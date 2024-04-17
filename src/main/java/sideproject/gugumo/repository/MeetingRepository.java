package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.meeting.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
