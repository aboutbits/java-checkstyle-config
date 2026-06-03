package fixtures.r4;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;

@Mapper
@AnnotateWith(MyUnmarked.class)
@MyUnmarked
public interface CustomNullUnmarkedGood {
}
