package fixtures.r4;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.AnnotateWith;

@MyMapper
@NullUnmarked
@AnnotateWith(NullUnmarked.class)
public interface CustomMapperBad {
}
