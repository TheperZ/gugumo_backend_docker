package sideproject.gugumo.domain.meeting;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalTime;

@Entity
@DiscriminatorValue("LongMeeting")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LongMeeting extends Meeting{
    private String meetingDays;             //장기

    @Override
    public void update(UpdatePostReq updatePostReq) {
        super.update(updatePostReq);
        this.meetingDays = updatePostReq.getMeetingDays();
    }
}
