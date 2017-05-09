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
    public static void main( String[] args )
    {
		String datasetRating = "data/Testes/ratingModel.csv";
    	String datasetUserGenre = "data/Testes/userGenreModel.csv";

    	/*//Etapa 0 - Teste 1 - 100% do dataset e 90% das avaliações
    	double evaluationPercentage = 1.0;//controls how many of the users are used in  evaluation
    	double trainingPercentage = 0.9; //percentage of each user's preferences to use to produce recommendations		
		Step0(datasetRating, evaluationPercentage, trainingPercentage);*/
		
    	//Etapa 0 - Teste 2 - 50% do dataset e 50% das avaliações
    	//double evaluationPercentage2 = 0.5;
    	//double trainingPercentage2 = 0.5; 
		//Step0(datasetRating, evaluationPercentage2, trainingPercentage2);
    	
		String datasetRating100Users = "data/rating100Users.csv", datasetUserGenre100users = "data/userGenre100UsersNormalized.csv";
    	//Etapa 1 - Teste 1 - 100% do dataset e 90% das avaliações
    	//double evaluationPercentage3 = 1.0, trainingPercentage3 = 0.9; 
		//Step1(datasetRating100Users, datasetUserGenre100users, evaluationPercentage3, trainingPercentage3);
		
    	//Filtragem Colaborativa Tradicional
    	FCTraditional(datasetRating);
    	
    	System.out.println("\n");
    	//Filtragem Colaborativa Com Entrada de Gêneros Implicito
		FCTraditionalValueImplicit(datasetRating, datasetUserGenre);
    	    	
    }
    
    public static void FCTraditional(String inputFileRating){
    	 UserSimilarity similarity;
         UserNeighborhood neighborhood;
         UserBasedRecommender recommender;
          
         try {
        	 FileDataModel model = new FileDataModel(new File(inputFileRating));
        	 
             similarity = new PearsonCorrelationSimilarity(model);
             neighborhood = new ThresholdUserNeighborhood(0.0, similarity, model);
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
         } catch (IOException e) {
     		System.out.println("There was an IO exception.");
 			e.printStackTrace();
     	} catch (TasteException e) {
     		System.out.println("There was an Taste exception.");
 			e.printStackTrace();
     	}
	    	
    }
    public static void FCTraditionalValueImplicit(String inputFileRating, String inputFileUserGenre){
   	 UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	
        	FileDataModel modelUserGenre = new FileDataModel(new File(inputFileUserGenre));
        	similarity = new PearsonCorrelationSimilarity(modelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(0.0, similarity, modelUserGenre);

            FileDataModel modelRating = new FileDataModel(new File(inputFileRating));
            recommender = new GenericUserBasedRecommender(modelRating, neighborhood, similarity);
            
            //Imprime usuários similares
             
            for(LongPrimitiveIterator users=modelRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
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
    public static void Step1(String inputFileUserItenRating, String inputFileUserGenre, double evaluationPercentage, double trainingPercentage) 
    {
    	
    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
    		public Recommender buildRecommender(DataModel model)throws TasteException
    		{	
    			//Calcula a Similaridade Utilizando a Correlação de Pearson
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);//UserGenre
				//Agrupa os Vizinhos Próximos
    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);//UserGenre
    			    			
    			String inputFileUserItenRating = "data/rating100Users.csv";
				FileDataModel dataModelRating = new FileDataModel(new File(inputFileUserItenRating));
				//Gera as Recomendações Baseado no Modelo de Avaliações, Vizinhança e Similaridade
    			Recommender recommender = new GenericUserBasedRecommender(dataModelRating, neighborhood, similarity);//UserRating
    			return recommender;
    		}
    	};    	
    	
    	/*DataModelBuilder modelBuilder = new DataModelBuilder() {
	        public DataModel buildDataModel( FastByIDMap<PreferenceArray> trainingData ) {
	            return new GenericBooleanPrefDataModel( GenericBooleanPrefDataModel.toDataMap(trainingData) );
	        }        
	    };*/
	    
    	try {
    		
    		long initialTime = System.currentTimeMillis();
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(inputFileUserItenRating));
			FileDataModel dataModelUserGenre = new FileDataModel(new File(inputFileUserGenre));
			
    		RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator();
    		double evaluetion_rmse = evaluator1.evaluate(userSimRecBuilder,null,dataModelUserGenre, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);
    		
    		RecommenderEvaluator evaluator12 = new RMSRecommenderEvaluator();
    		double evaluetion_rmse2 = evaluator12.evaluate(userSimRecBuilder,null,dataModelUserGenre, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse2);
    		
    		/*RecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		double evaluetion_aade = evaluator2.evaluate(userSimRecBuilder, null, dataModelUserGenre, trainingPercentage, evaluationPercentage);
    		System.out.println("AADE: " + evaluetion_aade);*/
    		
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
