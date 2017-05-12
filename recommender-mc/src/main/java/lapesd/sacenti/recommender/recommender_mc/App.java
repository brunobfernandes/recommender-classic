package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

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
	public static FileDataModel dataModelUserGenre;
	
    public static void main( String[] args )
    {
		String datasetUserItenRating = "data/rating100Users.csv";    	
		double evaluationPercentage = 0.1;//controls how many of the users are used in  evaluation
    	double trainingPercentage = 0.3; //percentage of each user's preferences to use to produce recommendations		
    	
    	
		//Filtragem Colaborativa Tradicional
    	//FCTraditional(datasetUserItenRating);
    	
    	//Filtragem Colaborativa Com Entrada de Gêneros Implicito
		//FCGenreValueImplicit(datasetUserItenRating);
		
		//Etapa 0 - FC Tradicional
    	//Step0(datasetUserItenRating, evaluationPercentage, trainingPercentage);
    			
    	//Etapa 1 - FC Valores Implícitos
		//Step1(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    }
    
    public static void FCTraditional(String inputFileRating){
    	 UserSimilarity similarity;
         UserNeighborhood neighborhood;
         UserBasedRecommender recommender;
          
         try {
        	 FileDataModel model = new FileDataModel(new File(inputFileRating));
        	 
             similarity = new PearsonCorrelationSimilarity(model);
             neighborhood = new ThresholdUserNeighborhood(-1.0, similarity, model);
             recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
             
             //Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<5; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 2);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
		         System.out.println("");
	         }
	         System.out.print("\n");
         } catch (IOException e) {
     		System.out.println("There was an IO exception.");
 			e.printStackTrace();
     	} catch (TasteException e) {
     		System.out.println("There was an Taste exception.");
 			e.printStackTrace();
     	}
	    	
    }
    public static void FCGenreValueImplicit(String inputFileRating){
   	 	UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File("data/userGenre100UsersNormalized.csv"));
        	similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(0.0, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(inputFileRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
            //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
	             }
	  
	         }
	         long[] theNeighborhood;
	         for(int idUser=1; idUser<5; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 2);//Qtd de Itens a recomendar         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
		         System.out.println("");
	         }
	         System.out.print("\n");
        } catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
	    	
   }
    
    
    public static void Step0(String inputFile, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Traditional: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.4, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    		System.out.println("Tempo de Duração: "+processingTimeGrouping +" milisegundos ou "+ processingTimeGrouping/1000+" segundos");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(inputFile));
    		
    		RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator();
    		//recommenderBuilder; dataModelBuilder; dataModel; trainingPercentage; evaluationPercentage
    		double evaluetion_rmse = evaluator1.evaluate(userSimRecBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);

    		RecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		double evaluetion_aade = evaluator2.evaluate(userSimRecBuilder, null, dataModelUserItenRating, trainingPercentage, evaluationPercentage);
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
    		/*RecommenderIRStatsEvaluator evaluator3 = new GenericRecommenderIRStatsEvaluator();
    		//recommenderBuilder; dataModelBuilder; dataModel; rescorer; at; relevanceThreshold; evaluationPercentage
    		IRStatistics medidaAvaliacao = evaluator3.evaluate(userSimRecBuilder, null, dataModelUserItenRating, null, 10, 5, evaluationPercentage);
    	    System.out.println("Precision: "+ medidaAvaliacao.getPrecision());
    	    System.out.println("Recall: "+ medidaAvaliacao.getRecall());
    	    System.out.println("FallOut: "+ medidaAvaliacao.getFallOut());
    	    System.out.println("F1Measure: "+ medidaAvaliacao.getF1Measure());
    	    System.out.println("FNMeasure: "+ medidaAvaliacao.getFNMeasure(2.0));
    	    System.out.println("NDCG: "+ medidaAvaliacao.getNormalizedDiscountedCumulativeGain());
    	    System.out.println("Reach: "+ medidaAvaliacao.getReach());*/
  	    
    	    
    	    long finalTime = System.currentTimeMillis();
    	    long processingTime = (finalTime - initialTime);
    		System.out.println("Tempo de Duração Total: "+processingTime +" milisegundos ou "+ processingTime/1000+" segundos\n\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }   
    public static void Step1(String inputFileUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 1: FC Implicit Input of the Genres: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    	
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			try {
						dataModelUserGenre = new FileDataModel(new File("data/userGenre100UsersNormalized.csv"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
    				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.4, similarity, dataModelUserGenre);    			    		
    				
    				long finalTimeGrouping = System.currentTimeMillis();
    	    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
    	    		System.out.println("Tempo de Duração: "+processingTimeGrouping +" milisegundos ou "+ processingTimeGrouping/1000+" segundos");
    				
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	}; 
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(inputFileUserItenRating));
			
    		RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator();
    		double evaluetion_rmse = evaluator1.evaluate(userSimRecBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);
    		
    		RecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		double evaluetion_aade = evaluator2.evaluate(userSimRecBuilder, null, dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("AADE: " + evaluetion_aade);
    		
    		
    		/*RecommenderIRStatsEvaluator evaluator3 = new GenericRecommenderIRStatsEvaluator();
    		//recommenderBuilder; dataModelBuilder; dataModel; rescorer; at; relevanceThreshold; evaluationPercentage
    		IRStatistics medidaAvaliacao = evaluator3.evaluate(userSimRecBuilder, null, dataModelUserItenRating, null, 10, 5, evaluationPercentage);
    	    System.out.println("Precision: "+ medidaAvaliacao.getPrecision());
    	    System.out.println("Recall: "+ medidaAvaliacao.getRecall());
    	    System.out.println("FallOut: "+ medidaAvaliacao.getFallOut());
    	    System.out.println("F1Measure: "+ medidaAvaliacao.getF1Measure());
    	    System.out.println("FNMeasure: "+ medidaAvaliacao.getFNMeasure(2.0));
    	    System.out.println("NDCG: "+ medidaAvaliacao.getNormalizedDiscountedCumulativeGain());
    	    System.out.println("Reach: "+ medidaAvaliacao.getReach());*/
    		
    		long finalTime = System.currentTimeMillis();
    	    long processingTime = (finalTime - initialTime);
    		System.out.println("Tempo de Duração Total: "+processingTime +" milisegundos ou "+ processingTime/1000+" segundos\n\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }
    
     
}
