package backend.academy.samples.multithreaded;

import backend.academy.FractalImage;
import backend.academy.singlethreaded.CreatorFlame;
import backend.academy.multithreaded.MultithreadedCreatorFlame;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class  MultithreadedCreatorFlameTest {

    @Test
    void comparePerformance_singleThreaded_vs_multiThreaded() {
        int numberIter = 1000;
        int numberSymmetry = 2;
        int imageWidth = 400;
        int imageHeight = 300;
        int numTransformations = 5;

        FractalImage fractalImageSingle = FractalImage.create(imageWidth, imageHeight, false);
        FractalImage fractalImageMulti = FractalImage.create(imageWidth, imageHeight, false);

        List<Transformation> transformations = new ArrayList<>();
        for (int i = 0; i < numTransformations; i++) {
            transformations.add(point -> point);
        }

        CreatorFlame singleThreadedCreator = new CreatorFlame();
        MultithreadedCreatorFlame multiThreadedCreator = new MultithreadedCreatorFlame();

        long startSingle = System.nanoTime();
        singleThreadedCreator.create(fractalImageSingle, numberIter, numberSymmetry, transformations);
        long endSingle = System.nanoTime();

        long startMulti = System.nanoTime();
        multiThreadedCreator.create(fractalImageMulti, numberIter, numberSymmetry, transformations);
        long endMulti = System.nanoTime();

        long singleThreadedDuration = endSingle - startSingle;
        long multiThreadedDuration = endMulti - startMulti;

        System.out.printf("Single-threaded duration: %.3f ms%n", singleThreadedDuration / 1_000_000.0);
        System.out.printf("Multi-threaded duration: %.3f ms%n", multiThreadedDuration / 1_000_000.0);

        assertThat(multiThreadedDuration).isLessThan(singleThreadedDuration);
    }
}
