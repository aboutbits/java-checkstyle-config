package fixtures.r4;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;

@Mapper
@AnnotateWith(NullUnmarked.class)
@NullUnmarked
public interface GoodMapper {
}
