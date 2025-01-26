package backend.academy.samples.singlethreaded;

import backend.academy.Changer;
import backend.academy.Creator;
import backend.academy.FractalImage;
import backend.academy.transformations.AthenianTransformation;
import backend.academy.transformations.Transformation;
import backend.academy.singlethreaded.CreatorFlame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatorFlameTest {

    private FractalImage mockFractal;

    @BeforeEach
    void setUp() {
        mockFractal = mock(FractalImage.class);
    }

    @Test
    void getAthen_shouldReturnSetOfAthenianTransformations() throws Exception {
        CreatorFlame creatorFlame = new CreatorFlame();
        int n = 5;

        var method = CreatorFlame.class.getDeclaredMethod("getAthen", int.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Set<AthenianTransformation> result = (Set<AthenianTransformation>) method.invoke(creatorFlame, n);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(n);
        for (AthenianTransformation transformation : result) {
            assertThat(transformation).isNotNull();
        }
    }

    @Test
    void create_shouldHandleEmptyTransformationList() {
        int numberIter = 10;
        int numberSymmetry = 6;
        List<Transformation> transformations = new ArrayList<>();

        CreatorFlame creatorFlame = new CreatorFlame();

        creatorFlame.create(mockFractal, numberIter, numberSymmetry, transformations);

        verifyNoInteractions(mockFractal);
    }

    @Test
    void create_shouldCallCreatorForEachTransformation() {
        int numberIter = 10;
        int numberSymmetry = 6;
        Transformation transformation1 = mock(Transformation.class);

        Creator mockCreator = spy(new Creator(mockFractal, new Changer(transformation1), new HashSet<>(), numberIter, numberSymmetry));
        doNothing().when(mockCreator).create();
    }
}
