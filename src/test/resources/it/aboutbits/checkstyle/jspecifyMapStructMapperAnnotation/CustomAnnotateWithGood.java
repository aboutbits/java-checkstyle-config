package fixtures.r4;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.Mapper;

@Mapper
@MyAnnotateWith(NullUnmarked.class)
@NullUnmarked
public interface CustomAnnotateWithGood {
}
