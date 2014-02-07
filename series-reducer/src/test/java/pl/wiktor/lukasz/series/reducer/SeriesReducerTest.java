package pl.wiktor.lukasz.series.reducer;

import static java.util.Arrays.asList;
import static pl.wiktor.lukasz.series.reducer.PointImpl.p;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SeriesReducerTest {
  
    @DataProvider(name="series")
    public Object[][] seriesDataProvider() {
        return new Object[][] {
                // 1.
                {asList(p(1, 1.0), p(2, 1.1), p(3, 0.9), p(4, 1.0)), /* epsilon = */ 0.2,
                 asList(p(1, 1.0), p(4, 1.0))},
                 
                // 2. 
                {asList(p(1, 1.0), p(2, 1.1), p(3, 0.9), p(4, 1.0)), /* epsilon = */ 0.05,
                 asList(p(1, 1.0), p(2, 1.1), p(3, 0.9), p(4, 1.0))},
                 
                // 3. 
                {asList(p(1, 1.0), p(2, 1.1), p(3, 1.2), p(4, 1.1), p(5, 1.0)), /* epsilon = */ 0.1,
                 asList(p(1, 1.0), p(3, 1.2), p(5, 1.0))}, 
        };
    }
    
    @Test(dataProvider="series")
    public void shouldReduceSeries(List<Point> inputSeries, double epsilon, List<Point> expectedResult) {
        List<Point> reducedSeries = SeriesReducer.reduce(inputSeries, epsilon);
        Assert.assertEquals(reducedSeries, expectedResult);
    }
    
    @DataProvider(name="invalidEpsilon")
    public Object[][] invalidEpsilonDataProvider() {
        return new Object[][] {{0.0}, {-0.1}, {-2.0}};
    }
    
    @Test(dataProvider="invalidEpsilon", expectedExceptions=IllegalArgumentException.class)
    public void shouldValidateEpsilon(double invalidEpsilon) {
        SeriesReducer.reduce(asList(p(1, 1), p(2, 2), p(3, 3)), invalidEpsilon);
    }
    
    @DataProvider(name="pointsAndLines")
    public Object[][] pointsAndLinesDataProvider() {
        return new Object[][] {
                //   point  |  lineStart | lineEnd | expectedDistance
                {p(1.0, 1.0), p(0.0, 0.0), p(2.0, 0.0), 1.0}, // horizontal line
                {p(1.0, 1.0), p(0.0, 0.0), p(0.0, 1.0), 1.0}, // vertical line
                {p(1.0, 1.0), p(-1.0, 1.0), p(1.0, -1.0), Math.sqrt(2)}, // sloped line
                {p(0.0, 0.0), p(-1.0, 1.0), p(1.0, -1.0), 0.0}, // point is on the line
        };
    }
    
    @Test(dataProvider="pointsAndLines")
    public void shouldCalculateDistanceFromPointToLine(Point p, Point lineStart, Point lineEnd, double expectedDistance) {
        double calculatedDistance = SeriesReducer.distance(p, lineStart, lineEnd); 
        Assert.assertTrue(Math.abs(expectedDistance - calculatedDistance) < 0.000000000000001);
    }
    
}
