package lapesd.sacenti.recommender.recommender_mc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
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
    	String etapa0_entrada1 = "data/simple-dataset.csv";
    	RecomendacaoFiltragemColaborativa(etapa0_entrada1);
    }
    
    
    public static void RecomendacaoFiltragemColaborativa(String inputFile) 
    {
    	/*DataModel model;
    	UserSimilarity similarity;
    	UserNeighborhood neighborhood;
    	UserBasedRecommender recommender;
    	
    	try {
			model = new FileDataModel(new File(entrada));
	    	similarity = new PearsonCorrelationSimilarity(model);
	    	neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
	    	recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		}	
		*/
    	//Controi o ambiente para testar as recomendações
    	RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
    		public Recommender buildRecommender(DataModel model)throws TasteException
    		{
    			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
    			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
    	   		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
    			return recommender;
    		}
    	};

    	try {
    		FileDataModel dataModel = new FileDataModel(new File(inputFile));
    		//(Detalhes da técnica; Padrão; Conjunto de dados;  %preferências por usuários; %usuários) 
    		
    		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
    		double userSimEvaluationScore = evaluator.evaluate(userSimRecBuilder,null,dataModel, 1, 0.5);
    		System.out.println("Avaliação de Similaridade de Usuários : "+userSimEvaluationScore);
    		
    		
    		/*
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
	    	//Especifica o Id do usuário e a qtd de itens a recomendar
	    	List<RecommendedItem> recommendations = recommender.recommend(2, 5);	    	
	    	for (RecommendedItem recommendationUser : recommendations) {
	    	  System.out.println(recommendationUser);
	    	}*/
    		
    	} catch (IOException e) {
    		System.out.println("There was an IO exception.");
			e.printStackTrace();
    	} catch (TasteException e) {
    		System.out.println("There was an Taste exception.");
			e.printStackTrace();
    	}
    }    
}
