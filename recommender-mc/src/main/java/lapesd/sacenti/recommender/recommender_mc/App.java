package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class App
{
    public static void main( String[] args )
    {
    	String dataset = "data/rating-dataset.csv";
    	
    	//Etapa 0 - Teste 1 - 90% do dataset e 100% das avaliações
    	double evaluationPercentage = 1.0;//controls how many of the users are used in  evaluation
    	double trainingPercentage = 0.9; //percentage of each user's preferences to use to produce recommendations		
		Step0(dataset, evaluationPercentage, trainingPercentage);
		
    	//Etapa 0 - Teste 2 - 50% do dataset e 50% das avaliações
    	double evaluationPercentage2 = 0.5;
    	double trainingPercentage2 = 0.5; 
		Step0(dataset, evaluationPercentage2, trainingPercentage2);
    }
    
    public static void Step0(String inputFile, double evaluationPercentage, double trainingPercentage) 
    {
    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
    		public Recommender buildRecommender(DataModel model)throws TasteException
    		{
    			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.4, similarity, model);
    	   		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
    			return recommender;
    		}
    	};

    	try {
    		
    		long initialTime = System.currentTimeMillis();
    		FileDataModel dataModel = new FileDataModel(new File(inputFile));
    		
    		RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator();
    		//recommenderBuilder; dataModelBuilder; dataModel; trainingPercentage; evaluationPercentage
    		double evaluetion_rmse = evaluator1.evaluate(userSimRecBuilder,null,dataModel, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);

    		RecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		double evaluetion_aade = evaluator2.evaluate(userSimRecBuilder, null, dataModel, trainingPercentage, evaluationPercentage);
    		System.out.println("AADE: " + evaluetion_aade);
    		
    		
    		/*For each user, these implementation determine the top n preferences, 
    		 * then evaluate the IR statistics based on a DataModel that does not have 
    		 * these values. This number n is the "at" value, as in "precision at 5". 
    		 * For example, this would mean precision evaluated by removing the 
    		 * top 5 preferences for a user and then finding the percentage of 
    		 * those 5 items included in the top 5 recommendations for that user.
    		 * */
    		/*GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD = Pass as "relevanceThreshold" 
    		 * argument to to have it attempt to compute a reasonable threshold. Note that this will impact performance.
    		 */
    		/*RESCORER (if any, to use when computing recommendations), 
    		 * AT (n de recom. consideradas na avaliação das métricas), 
    		 * RELEVANCETHRESHOLD (items whose preference value is at least this value are considered "relevant" for the purposes of computations) 
    		 * ASSESSMENTPERCENTAGE (porcentage of evaluation).
    		 * */
    		// evaluate precision recall, etc. at 10
    		//???????????relevanceThreshold – items whose preference value is at least this value are considered “relevant” for the purposes of computations
    		RecommenderIRStatsEvaluator evaluator3 = new GenericRecommenderIRStatsEvaluator();
    		//recommenderBuilder; dataModelBuilder; dataModel; rescorer; at; relevanceThreshold; evaluationPercentage
    		IRStatistics medidaAvaliacao = evaluator3.evaluate(userSimRecBuilder, null, dataModel, null, 10, 5, evaluationPercentage);
    	    System.out.println("Precision: "+ medidaAvaliacao.getPrecision());
    	    System.out.println("Recall: "+ medidaAvaliacao.getRecall());
    	    System.out.println("FallOut: "+ medidaAvaliacao.getFallOut());
    	    System.out.println("F1Measure: "+ medidaAvaliacao.getF1Measure());
    	    System.out.println("FNMeasure: "+ medidaAvaliacao.getFNMeasure(2.0));
    	    System.out.println("NDCG: "+ medidaAvaliacao.getNormalizedDiscountedCumulativeGain());
    	    System.out.println("Reach: "+ medidaAvaliacao.getReach());
  	    
    	    
    	    long finalTime = System.currentTimeMillis();
    	    long processingTime = (finalTime - initialTime)/1000;
    		System.out.println("Tempo de Duração: "+processingTime +" segundos ou "+ processingTime/60+" minutos\n\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }    
}
