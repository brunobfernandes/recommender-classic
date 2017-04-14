package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
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
        showItemRecommender();
    }
    
    // From tutorial <https://mahout.apache.org/users/recommender/userbased-5-minutes.html>
    public static void showUserRecommender() 
    {
    	DataModel model;
    	UserSimilarity similarity;
    	UserNeighborhood neighborhood;
    	UserBasedRecommender recommender;
    	
    	try {
			model = new FileDataModel(new File("data/simple-dataset.csv"));
	    	similarity = new PearsonCorrelationSimilarity(model);
	    	neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
	    	recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    
	    	List<RecommendedItem> recommendations = recommender.recommend(2, 3);
	    	for (RecommendedItem recommendation : recommendations) {
	    	  System.out.println(recommendation);
	    	}
	    } catch (IOException e) {
			System.out.println("There was an IO exception.");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was an Taste exception.");
			e.printStackTrace();
		}    
    }
    
    // from tutorial <https://www.youtube.com/watch?v=yD40rVKUwPI>
    public static void showItemRecommender() 
    {
    	DataModel model;
    	ItemSimilarity similarity;
    	GenericItemBasedRecommender recommender;
    	try {
			model = new FileDataModel(new File("data/simple-dataset.csv"));
	    	similarity = new LogLikelihoodSimilarity(model);
	    	recommender = new GenericItemBasedRecommender(model, similarity);
	    
	    	for(LongPrimitiveIterator items = model.getItemIDs(); items.hasNext();) {
	    		long itemId = items.nextLong();
	    		
	    		List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, 3);
	    		for (RecommendedItem recommendation : recommendations) {
	    			System.out.println(itemId + ", " + recommendation.getItemID() + ", " + recommendation.getValue());
	    		}
	    	}
	    } catch (IOException e) {
			System.out.println("There was an IO exception.");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was an Taste exception.");
			e.printStackTrace();
		}    
    }
    
    // From article <https://arxiv.org/ftp/arxiv/papers/1503/1503.06562.pdf>
    public static void showMultiCriteriaRecommender() 
    {
    	System.out.println("Under construction...");
    }
}
