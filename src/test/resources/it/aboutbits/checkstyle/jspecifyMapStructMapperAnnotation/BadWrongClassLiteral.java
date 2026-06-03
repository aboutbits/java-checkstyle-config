package fixtures.r4;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;

@Mapper
@AnnotateWith(NullMarked.class)
@NullUnmarked
public interface BadWrongClassLiteral {
}
