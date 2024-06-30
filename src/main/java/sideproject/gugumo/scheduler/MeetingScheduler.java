package sideproject.gugumo.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.meeting.Meeting;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;
import sideproject.gugumo.repository.MeetingRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingScheduler {

    private final MeetingRepository meetingRepository;

    //매일 마감일이 지난 모임 정보를 모집마감으로 변경
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void setExpiredMeetingStatus() {

        LocalDate today = LocalDate.now();

        List<Meeting> targetMeeting = meetingRepository.findByMeetingDeadlineBeforeAndStatus(today, MeetingStatus.RECRUIT);


        for (Meeting meeting : targetMeeting) {
            meeting.expireStatus();
        }


    }

}
