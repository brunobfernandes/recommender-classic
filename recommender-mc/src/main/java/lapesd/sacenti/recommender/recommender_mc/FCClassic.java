package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
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
	public static double threshold = 0.7;
	public static long processingTimeGroupingTotal;
	
	public static void Webmedia_Evaluation_PearsonCorrelation(String datasetUserItenRating){
		System.out.println("##################################");
		System.out.println("FC Classic - Pearson Correlation");
		try {
		
			RandomUtils.useTestSeed(); // to randomize the evaluation result        
			DataModel model = new FileDataModel(new File(datasetUserItenRating));
	
			RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel model) throws TasteException {                
					UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
					UserNeighborhood neighborhood = new NearestNUserNeighborhood (100, similarity, model);                
					return new GenericUserBasedRecommender(model, neighborhood, similarity);                
	        }
			};
	    
		    RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		    double evaluetion_rmse = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
		    System.out.println("RMSE: " + evaluetion_rmse);
		    
		    //RecommenderEvaluator evaluator1 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		//double evaluetion_aade = evaluator1.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
    		//System.out.println("AADE: " + evaluetion_aade);
		    
		    /*RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
		    IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, model, null, 10, 4, 0.7); // evaluate precision recall at 10
		    System.out.println("Precision: " + stats.getPrecision());
		    System.out.println("Recall: " + stats.getRecall());
		    System.out.println("F1 Score: " + stats.getF1Measure());  */
		} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}    
	}
	
	
		
	public static void vizinhanca(DataModel model, UserNeighborhood neighborhood, UserSimilarity similarity) throws TasteException {
		int Naonan=0;
		int usuarionaoexiste=0;
		for (int i=2;i<model.getNumUsers();i++) {
			try {
				if (!Double.isNaN(similarity.userSimilarity(1,i))) {
		//			System.out.println("Similaridade entre " + 1 + " e " + i + ": " + similarity.userSimilarity(1,i));
					Naonan++;
				}
    		} catch (TasteException e) {
    			usuarionaoexiste++;
    		}
		}
		System.out.println("Total de Usuários: " + model.getNumUsers());

		System.out.println("Total de Itens: " + model.getNumItems());
		System.out.println("Vizinhos com similaridade com U1: " + Naonan);
		System.out.println("Total de Usuários que não existem: " + usuarionaoexiste);
		
		int somavizinhos = 0;
		int semvizinhos = 0;
		int comException = 0;
		long[] vizinhanca;
		for (int i=1;i<=model.getNumUsers();i++) {
			try {
				vizinhanca = neighborhood.getUserNeighborhood(i);
				if (vizinhanca!=null)
				somavizinhos += vizinhanca.length;
				if (vizinhanca.length==0)
					semvizinhos +=1;
    		} catch (TasteException e) {
    			comException++;
    		}
		}
		System.out.println("Média da vizinhança: " + somavizinhos/model.getNumUsers());
		System.out.println("Número de usuários sem vizinhos: " + semvizinhos);
		System.out.println("Número de usuários com TasteException: " + comException);
	}
	
	
	public static void FCPearsonCorrelation(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) {
		System.out.println("############################################################################################");
		System.out.println("Step 0: FC Classic - Pearson Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
		processingTimeGroupingTotal = 0;
		try {
		FileDataModel model = new FileDataModel(new File(datasetUserItenRating));

		long initialTimeGrouping = System.nanoTime();
		long initialTimeSymilarity = System.nanoTime();
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		long similarityTime = System.nanoTime()-initialTimeSymilarity;
		long initialTimeneighborhood = System.nanoTime();
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
		//UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
		long UserNeighborhoodTime = System.nanoTime()-initialTimeneighborhood;
		long processingTimeGrouping = (System.nanoTime() - initialTimeGrouping);
		FCClassic.vizinhanca(model,neighborhood,similarity);
		System.out.println("Similarity Duration: "+ similarityTime/1000+" ms");
		System.out.println("Neighborhood Duration: "+ UserNeighborhoodTime/1000+" ms");	    			
		System.out.println("Grouping Duration: "+ processingTimeGrouping/1000+" ms");
//		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);		
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
  			System.out.println("############################################################################################");
    		System.out.println("Step 0: FC Classic - Pearson Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		RandomUtils.useTestSeed(); // to randomize the evaluation result
	    	DataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
    		
    		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.nanoTime();
	    			long initialTimeSymilarity = System.nanoTime();
	    			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
					long similarityTime = System.nanoTime()-initialTimeSymilarity;
					long initialTimeneighborhood = System.nanoTime();
					//UserNeighborhood neighborhood = new NearestNUserNeighborhood(70, similarity, model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
					long UserNeighborhoodTime = System.nanoTime()-initialTimeneighborhood;
					long processingTimeGrouping = (System.nanoTime() - initialTimeGrouping);

					FCClassic.vizinhanca(model,neighborhood,similarity);
    				System.out.println("Similarity Duration: "+ similarityTime/1000+" ms");
    				System.out.println("Neighborhood Duration: "+ UserNeighborhoodTime/1000+" ms");	    			
    				System.out.println("Grouping Duration: "+ processingTimeGrouping/1000+" ms");
	
    				Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	}; 
    		// Recommend certain number of items for a particular user
  	        // Here, recommending 5 items to user_id = 9
  	        /*Recommender recommender = recommenderBuilder.buildRecommender(model);
  	        List<RecommendedItem> recomendations = recommender.recommend(9, 5);
  	        for (RecommendedItem recommendedItem : recomendations) {
  	            System.out.println(recommendedItem);    
  	        }*/
    		
    		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
    		double evaluetion_rmse = evaluator.evaluate(recommenderBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);
    		
    		evaluetion_rmse = evaluator.evaluate(recommenderBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);
    		
    		evaluetion_rmse = evaluator.evaluate(recommenderBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);

    		//RecommenderEvaluator evaluator1 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		//double evaluetion_aade = evaluator1.evaluate(recommenderBuilder, null, dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		//System.out.println("AADE: " + evaluetion_aade);
    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}     		
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
    		IRStatistics medidaAvaliacao = evaluator3.evaluate(recommenderBuilder, null, dataModelUserItenRating, null, 10, 4, evaluationPercentage);
    	    System.out.println("Precision: "+ medidaAvaliacao.getPrecision());
    	    System.out.println("Recall: "+ medidaAvaliacao.getRecall());
    	    System.out.println("FallOut: "+ medidaAvaliacao.getFallOut());
    	    System.out.println("F1Measure: "+ medidaAvaliacao.getF1Measure());
    	    System.out.println("FNMeasure: "+ medidaAvaliacao.getFNMeasure(2.0));
    	    System.out.println("NDCG: "+ medidaAvaliacao.getNormalizedDiscountedCumulativeGain());
    	    System.out.println("Reach: "+ medidaAvaliacao.getReach());*/
  	    
    	    
 //   	    long finalTime = System.currentTimeMillis();
//    	    long processingTime = (finalTime - initialTime);
//    		System.out.println("Total Grouping Duration: "+processingTimeGroupingTotal +" milliseconds or "+ processingTimeGroupingTotal/1000+" seconds");
 //   	    System.out.println("Total Duration Time: "+processingTime +" milliseconds or "+ processingTime/1000+" seconds\n");
	    		    		

    }  
	public static void Evaluation_SpearmanCorrelation(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) 
    {
    	try {
    		System.out.println("Etapa 0: FC Classic - Spearman Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		processingTimeGroupingTotal = 0;
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			FCClassic.vizinhanca( model,neighborhood,similarity);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    processingTimeGroupingTotal = processingTimeGroupingTotal +  processingTimeGrouping;
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
    	    System.out.println("Total Grouping Duration: "+processingTimeGroupingTotal +" milliseconds or "+ processingTimeGroupingTotal/1000+" seconds");
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
    		processingTimeGroupingTotal = 0;
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			FCClassic.vizinhanca( model,neighborhood, similarity);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    processingTimeGroupingTotal = processingTimeGroupingTotal +  processingTimeGrouping;
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
    	    System.out.println("Total Grouping Duration: "+processingTimeGroupingTotal +" milliseconds or "+ processingTimeGroupingTotal/1000+" seconds");
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
    		processingTimeGroupingTotal = 0;
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			FCClassic.vizinhanca( model,neighborhood, similarity);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    processingTimeGroupingTotal = processingTimeGroupingTotal +  processingTimeGrouping;
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
    	    System.out.println("Total Grouping Duration: "+processingTimeGroupingTotal +" milliseconds or "+ processingTimeGroupingTotal/1000+" seconds");
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
    		processingTimeGroupingTotal = 0;
    		
    		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new LogLikelihoodSimilarity(model);
	    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	    			
	    			FCClassic.vizinhanca( model,neighborhood, similarity);
	    			
	    			long finalTimeGrouping = System.currentTimeMillis();
		    	    long processingTimeGrouping = (finalTimeGrouping - initialTimeGrouping);
		    	    processingTimeGroupingTotal = processingTimeGroupingTotal +  processingTimeGrouping;
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
    	    System.out.println("Total Grouping Duration: "+processingTimeGroupingTotal +" milliseconds or "+ processingTimeGroupingTotal/1000+" seconds");
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