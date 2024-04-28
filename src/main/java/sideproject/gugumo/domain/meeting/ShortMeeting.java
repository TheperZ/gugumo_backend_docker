package sideproject.gugumo.domain.meeting;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sideproject.gugumo.request.UpdatePostReq;


@Entity
@DiscriminatorValue("ShortMeeting")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortMeeting extends Meeting {


    @Override
    public void update(UpdatePostReq updatePostReq) {
        super.update(updatePostReq);
    }
}
