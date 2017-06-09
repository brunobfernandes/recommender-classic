package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
import org.apache.mahout.common.RandomUtils;

public class FCProposal {
	public static double threshold = 0.9;
	public static FileDataModel dataModelUserGenre;
	public static String fileDataModelUserGenre = "data/userGenreNormalized-dataset.csv";
	public static long processingTimeGroupingTotal;
	
	public static void Webmedia_Evaluation_PearsonCorrelation(String datasetUserItenRating){
		System.out.println("FH Proposal - Pearson Correlation");
		try {
			RandomUtils.useTestSeed(); // to randomize the evaluation result        
			DataModel model = new FileDataModel(new File(datasetUserItenRating));
			dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
			
			RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel model) throws TasteException {
					UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
					//UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);
					UserNeighborhood neighborhood = new NearestNUserNeighborhood (100, similarity, dataModelUserGenre);                
					return new GenericUserBasedRecommender(model, neighborhood, similarity);                
				}
			};
	    
			RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		    double evaluetion_rmse = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
		    System.out.println("RMSE: " + evaluetion_rmse);
		    
		    RecommenderEvaluator evaluator1 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    		double evaluetion_aade = evaluator1.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
    		System.out.println("AADE: " + evaluetion_aade);
			
			/*RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
	        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, model, null, 10, 4, 0.7); // evaluate precision recall at 10
		    System.out.println("Precision: " + stats.getPrecision());
		    System.out.println("Recall: " + stats.getRecall());
		    System.out.println("F1 Score: " + stats.getF1Measure());*/
		} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}    
	}
	public static void Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(String datasetUserItenRating){
		System.out.println("FH Proposal - Pearson Correlation");
		processingTimeGroupingTotal = 0;
		try {
			RandomUtils.useTestSeed(); // to randomize the evaluation result 
			FileDataModel model = new FileDataModel(new File(datasetUserItenRating));
			dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
	
			long initialTimeGrouping = System.nanoTime();
			long initialTimeSymilarity = System.nanoTime();
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
			long similarityTime = System.nanoTime()-initialTimeSymilarity;
			long initialTimeneighborhood = System.nanoTime();
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
			//UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, dataModelUserGenre);
			long UserNeighborhoodTime = System.nanoTime()-initialTimeneighborhood;
			long processingTimeGrouping = (System.nanoTime() - initialTimeGrouping);
			//FCClassic.vizinhanca(model,neighborhood,similarity);
			System.out.println("Similarity Duration: "+ similarityTime/1000+" ms");
			System.out.println("Neighborhood Duration: "+ UserNeighborhoodTime/1000+" ms");	    			
			System.out.println("Grouping Duration: "+ processingTimeGrouping/1000+" ms");
			//Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);	
			System.out.print("\n");
		        
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
	
	public static void FCPropostaPearsonCorrelation(String datasetUserItenRating, double evaluationPercentage, double trainingPercentage) {
		System.out.println("############################################################################################");
		System.out.println("Step 0: FC Proposal - Pearson Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
		processingTimeGroupingTotal = 0;
		try {
		FileDataModel model = new FileDataModel(new File(datasetUserItenRating));

		long initialTimeGrouping = System.nanoTime();
		long initialTimeSymilarity = System.nanoTime();
		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
		long similarityTime = System.nanoTime()-initialTimeSymilarity;
		long initialTimeneighborhood = System.nanoTime();		
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
		//UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, dataModelUserGenre);
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

	//FCGenreValueImplicit
	public static void PearsonCorrelation(String datasetUserItenRating){
		System.out.println("Pearson Correlation");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
        	similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
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
            /* //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
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
	
	public static void SpearmanCorrelation(String datasetUserItenRating){
		System.out.println("Spearman Correlation");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
        	similarity = new SpearmanCorrelationSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
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
            /* //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
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
	public static void EuclideanDistance(String datasetUserItenRating){
		System.out.println("Euclidean Distance");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
        	similarity = new EuclideanDistanceSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
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
            /* //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
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
	public static void TanimotoCoefficient(String datasetUserItenRating){
		System.out.println("Tanimoto Coefficient");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
        	similarity = new TanimotoCoefficientSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
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
            /* //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
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
	public static void LogLikelihood(String datasetUserItenRating){
		System.out.println("Log Likelihood");
		UserSimilarity similarity;
        UserNeighborhood neighborhood;
        UserBasedRecommender recommender;
         
        try {
        	dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
        	similarity = new LogLikelihoodSimilarity(dataModelUserGenre);
            neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);

            FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
            recommender = new GenericUserBasedRecommender(dataModelUserItenRating, neighborhood, similarity);
            
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
            /* //Imprime usuários similares
            for(LongPrimitiveIterator users=dataModelUserItenRating.getUserIDs(); users.hasNext(); )
	         {
	             long userId = users.nextLong();
	             long[] recommendedUserIDs = recommender.mostSimilarUserIDs(userId, 5); 
	              
	             for(long recId:recommendedUserIDs)
	             {
	                 System.out.println("Usuário "+userId+" similar com Usuário "+recId +" similaridade de : "+similarity.userSimilarity(userId, recId));
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
    		System.out.println("Step 1: FC Implicit Input of the Genres - Pearson Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		
    		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
    		
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.nanoTime();
	    			long initialTimeSymilarity = System.nanoTime();
					UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModelUserGenre);
					long similarityTime = System.nanoTime()-initialTimeSymilarity;
					long initialTimeneighborhood = System.nanoTime();
					//UserNeighborhood neighborhood = new NearestNUserNeighborhood(30, similarity, model);
					UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);    			    		
					long UserNeighborhoodTime = System.nanoTime()-initialTimeneighborhood;
					long processingTimeGrouping = (System.nanoTime() - initialTimeGrouping);

    				FCProposal.vizinhanca(model,neighborhood,similarity);
    				System.out.println("Similarity Duration: "+ similarityTime/1000+" ms");
    				System.out.println("Neighborhood Duration: "+ UserNeighborhoodTime/1000+" ms");	    			
    				System.out.println("Grouping Duration: "+ processingTimeGrouping/1000+" ms");
    				
	    			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	    			return recommender;
	    		}
	    	}; 
    		FileDataModel dataModelUserItenRating = new FileDataModel(new File(datasetUserItenRating));
			
    		RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator();
    		double evaluetion_rmse = evaluator1.evaluate(userSimRecBuilder,null,dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    		System.out.println("RMSE: "+evaluetion_rmse);
    		
    //		RecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
    //		double evaluetion_aade = evaluator2.evaluate(userSimRecBuilder, null, dataModelUserItenRating, trainingPercentage, evaluationPercentage);
    //		System.out.println("AADE: " + evaluetion_aade);
    		
    		
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
    		System.out.println("Step 1: FC Implicit Input of the Genres - Spearman Correlation: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		processingTimeGroupingTotal = 0;
    	
    		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
    		
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new SpearmanCorrelationSimilarity(dataModelUserGenre);
    				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);    			    		
    				
    				FCProposal.vizinhanca( model,neighborhood,similarity);

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
    		System.out.println("Step 1: FC Implicit Input of the Genres - Euclidean Distance: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		processingTimeGroupingTotal = 0;
    	
    		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
    		
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModelUserGenre);
    				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);    			    		
  
    				FCProposal.vizinhanca( model,neighborhood,similarity);

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
    		System.out.println("Step 1: FC Implicit Input of the Genres - Tanimoto Coefficient: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		processingTimeGroupingTotal = 0;
    	
    		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
    		
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModelUserGenre);
    				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);    			    		
    
    				FCProposal.vizinhanca( model,neighborhood,similarity);
 				
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
    		System.out.println("Step 1: FC Implicit Input of the Genres - Log Likelihood: "+evaluationPercentage+"% of dataset and "+trainingPercentage+"% User ratings");
    		long initialTime = System.currentTimeMillis();
    		processingTimeGroupingTotal = 0;
    	
    		dataModelUserGenre = new FileDataModel(new File(fileDataModelUserGenre));
    		
	    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
	    		public Recommender buildRecommender(DataModel model)throws TasteException
	    		{	
	    			long initialTimeGrouping = System.currentTimeMillis();
	    			
	    			UserSimilarity similarity = new LogLikelihoodSimilarity(dataModelUserGenre);
    				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModelUserGenre);    			    		
    				
    				FCProposal.vizinhanca( model,neighborhood,similarity);

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