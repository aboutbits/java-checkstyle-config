package fixtures.r4;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;

@Mapper
@NullUnmarked
@AnnotateWith(NullUnmarked.class)
public abstract class BadAbstractMapperOrderSwapped {
}
