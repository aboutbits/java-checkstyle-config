package fixtures.r4;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.AnnotateWith;

@MyMapper
@AnnotateWith(NullUnmarked.class)
@NullUnmarked
public interface CustomMapperGood {
}
