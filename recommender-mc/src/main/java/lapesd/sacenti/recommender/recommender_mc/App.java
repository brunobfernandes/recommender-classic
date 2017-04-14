package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        showUserRecommender();
    }
    
    public static void showUserRecommender() 
    {
    	DataModel model;
    	UserSimilarity similarity;
    	UserNeighborhood neighborhood;
    	UserBasedRecommender recommender;
    	
    	try {
			model = new FileDataModel(new File("./data/simple-dataset.csv"));
	    	similarity = new PearsonCorrelationSimilarity(model);
	    	neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
	    	recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    
	    	List<RecommendedItem> recommendations = recommender.recommend(2, 3);
	    	for (RecommendedItem recommendation : recommendations) {
	    	  System.out.println(recommendation);
	    	}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void showItemRecommender() 
    {
        System.out.println( "In construction!" );
    }
}
