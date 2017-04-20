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
    	/* DataModel = { USER, ITEM, PREFERENCE }
    	 * then, u in USER and p in PREFERENCE, u = {p1, p2, p3, p4 ,p5, ...}. 
    	 * 
    	 * The PearsonCorrelationSimilary define the similarity between user's vector u' and u'',
    	 * where sim(u', u'') = cos(u' angle u'').
    	 * 
    	 * The ThresholdUserNeighborhood of u' is an user group UN, 
    	 * where foreach u'' in UN, u'' != u' and sim(u', u'') > threshold.
    	 * 
    	 * A GenericUserBasedRecommender recommends to an user u' a list of top items TI,
    	 * where i in TI, u'' in UN(u') | i in u''.items and TI is ordered.
    	 * */
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
    	/* DataModel = { USER, ITEM, PREFERENCE }
    	 * 
    	 * The LogLikelihoodSimilary define the similarity between items i' and i'',
    	 * where sim(i', i'') = 1.0 - 1.0 / (1.0 + logLikelihoodRatio) and logLikelihoodRatio = (2.0 * (rowEntropy + columnEntropy - matrixEntropy))
    	 * 
    	 * A GenericUserBasedRecommender recommends to an item i' a list of top items TI,
    	 * where i'' in TI, sim(i', i'') and TI is ordered.
    	 * i.e.: returns items that have not been rated by the user and that were preferred by another user
    	 * that has preferred at least one item that the current user has preferred too
    	 * */
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
    	/* DataModel = { USER, ITEM, {PREFERENCEoverall, PREFERENCEcriteria1, PREFERENCEcriteria2, ...} }
    	 * ! Foreach u' in USER, u' =  { ITEM1.{PREFERENCEoverall, PREFERENCEcriteria1, PREFERENCEcriteria2, ... },
    	 * 								 ITEM2.{PREFERENCEoverall, PREFERENCEcriteria1, PREFERENCEcriteria2, ... },
    	 * 								 ... }
    	 * 
    	 * EstimatedOverallPreference is a estimated preference of i' for u' ,
    	 * where eOverallPreference(i', u') = 
    	 * 
    	 * MultiCriteriaRecommender has 2 steps: 1. predicting rating for a particular unseen item;
    	 * 2. recommending to an user u' a list of top items TI,
    	 * where i' in TI,  TI is ordered by eOverallPreference(i', u').
    	 * i.e.: returns items that have not been rated by the user, ordered by estimated preference of that user
    	 * 
    	 * Obs.: SVD, SSVD, PCA, HOSVD with map reduce.
    	 * 
    	 * */
    	System.out.println("Under construction...");
    }


	/* WANTED:
	 * DataModel = { USER, ITEM, !PREFERENCE }
	 * ! Foreach u' in USER, u' =  { ATRIBUTE(criteria)1.{estimativeVALUE1, estimativeVALUE2, ... },
	 * 								 ATRIBUTE(criteria)2.{estimativeVALUE1, estimativeVALUE2, ... },
	 * 								 ... }
	 * 
	 * EstimatedPreference is a estimated preference of i' for u' ,
	 * where ePref(i', u') = 
	 * 
	 * MultiCriteriaRecommender recommends to an user u' a list of top items TI,
	 * where i' in TI, ePref(i', u') and TI is ordered.
	 * i.e.: returns items that have not been rated by the user, ordered by estimated preference of that user
	 * */
    
    
}
