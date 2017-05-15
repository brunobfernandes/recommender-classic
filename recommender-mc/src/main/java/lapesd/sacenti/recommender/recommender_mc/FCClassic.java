package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class FCClassic {
	public static double threshold = 0.6;
	
	public static void PearsonCorrelation(String datasetUserItenRating) {
		System.out.println("Spearman Correlation");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
       	 	FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
       	 
            similarity = new PearsonCorrelationSimilarity(model);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
            //neighborhood = new NearestNUserNeighborhood(5, similarity, model);
           		 
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            
            long userId;
            long[] recommendedUserIDs;
            
            for(int i=1; i<=3; i++){
            	userId= i;
            	recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3);
            	
            	for(long recID:recommendedUserIDs)
                {
                    System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
                }
            }
            
            /*//Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }*/
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<=2; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 3);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
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
	public static void SpearmanCorrelation(String datasetUserItenRating) {
		/*Variação da medida Pearson Correlation, única diferença é que as avaliações 
		 * dos itens são recalculadas de acordo com ranking das avaliações iniciais antes 
		 * da expansão do cálculo da correlação.*/
		System.out.println("Spearman Correlation");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
    		FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
       	 	similarity = new SpearmanCorrelationSimilarity(model);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
            //neighborhood = new NearestNUserNeighborhood(5, similarity, model);
           		 
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            
            long userId;
            long[] recommendedUserIDs;
            
            for(int i=1; i<=3; i++){
            	userId= i;
            	recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3);
            	
            	for(long recID:recommendedUserIDs)
                {
                    System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
                }
            }
            /*//Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }*/
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<=2; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 3);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
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
	public static void EuclideanDistance(String datasetUserItenRating) {
		/*Baseado na distância entre usuários, aonde usuários são pontos em um 
		 * espaço de n (número de itens e valores de preferência como coordenadas) 
		 * dimensões. Computa a distância euclidiana d entre dois "pontos usuários". 
		 * */
		System.out.println("Euclidean Distance");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
       	 	FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
       	 
            similarity = new EuclideanDistanceSimilarity(model);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
            //neighborhood = new NearestNUserNeighborhood(5, similarity, model);
           		 
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            
            long userId;
            long[] recommendedUserIDs;
            
            for(int i=1; i<=3; i++){
            	userId= i;
            	recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3);
            	
            	for(long recID:recommendedUserIDs)
                {
                    System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
                }
            }
            
            /*//Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }*/
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<=2; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 3);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
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
	public static void TanimotoCoefficient(String datasetUserItenRating) {
		/*Implementação que ignora valores de preferência completamente, importando 
		 * apenas se o usuário expressa uma preferência. Essa medida é a razão do tamanho
		 *  da intersecção em relação ao tamanho da união dos itens que dois usuários demonstraram preferência.*/
		System.out.println("Tanimoto Coefficient");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
       	 	FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
       	 
            similarity = new TanimotoCoefficientSimilarity(model);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
            //neighborhood = new NearestNUserNeighborhood(5, similarity, model);
           		 
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            
            long userId;
            long[] recommendedUserIDs;
            
            for(int i=1; i<=3; i++){
            	userId= i;
            	recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3);
            	
            	for(long recID:recommendedUserIDs)
                {
                    System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
                }
            }
            /*//Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }*/
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<=2; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 3);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
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
	public static void LogLikelihood(String datasetUserItenRating) {
		/*Assim como a medida Tanimoto Coefficient, se baseia no número de itens 
		 * em comum entre dois usuários, mas seu valor é mais uma expressão do 
		 * quão improvável dois usuários tenham sobreposição, dado o número total de 
		 * itens e o número de itens que cada usuário possui preferência.*/
		System.out.println("LogLikelihood");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
       	 	FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
       	 
            similarity = new LogLikelihoodSimilarity(model);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
            //neighborhood = new NearestNUserNeighborhood(5, similarity, model);
           		 
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            
            long userId;
            long[] recommendedUserIDs;
            
            for(int i=1; i<=3; i++){
            	userId= i;
            	recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3);
            	
            	for(long recID:recommendedUserIDs)
                {
                    System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
                }
            }
            /*//Imprime usuários similares
	         for(LongPrimitiveIterator users=model.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 3); 
	              
	             for(long recID:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recID +" similaridade de : "+similarity.userSimilarity(userId, recID));
	             }
	  
	         }*/
	         long[] theNeighborhood;
	         //Especifica o Id do usuário e a qtd de itens a recomendar
	         for(int idUser=1; idUser<=2; idUser++){
	        	 theNeighborhood = neighborhood.getUserNeighborhood(idUser);
	        	 System.out.print("Usuário "+idUser+ " possui similaridade com os usuários: ");
		         for(long userSimilares: theNeighborhood){
		        	 System.out.print(userSimilares+" ,");
		         }
		         System.out.println("\nLista de Recomendação para o usuário: "+idUser);
		         List<RecommendedItem> recommendations = recommender.recommend(idUser, 3);         
		         for (RecommendedItem recommendationUser : recommendations) 
		         {
		           System.out.println(recommendationUser);
		         }
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
	
	public static void Evaluation_PearsonCorrelation(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Step 0: FC Classic - Pearson Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    		System.out.println("Grouping Duration: "+processingTimeGrouping +" milliseconds or "+ processingTimeGrouping/1000+" seconds");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
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
    		System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }  
	public static void Evaluation_SpearmanCorrelation(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Classic - Spearman Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    System.out.println("Grouping Duration: "+processingTimeGrouping +" milliseconds ou "+ processingTimeGrouping/1000+" seconds");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
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
    	    System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }
	public static void Evaluation_EuclideanDistance(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Classic - Euclidean Distance: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    System.out.println("Grouping Duration: "+processingTimeGrouping +" milliseconds ou "+ processingTimeGrouping/1000+" seconds");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
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
    	    System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }
	public static void Evaluation_TanimotoCoefficient(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Classic - Tanimoto Coefficient: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    System.out.println("Grouping Duration: "+processingTimeGrouping +" milliseconds ou "+ processingTimeGrouping/1000+" seconds");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
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
    	    System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }
	public static void Evaluation_LogLikelihood(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Classic - Tanimoto Coefficient: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new LogLikelihoodSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    System.out.println("Grouping Duration: "+processingTimeGrouping +" milliseconds ou "+ processingTimeGrouping/1000+" seconds");
	    			
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	};    		
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
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
    	    System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }
	
}