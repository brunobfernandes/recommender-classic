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
		String datasetUserItenRating = "data/rating604Users.csv";    	
		double evaluationPercentage = 1.0;//controls how many of the users are used in  evaluation
    	double trainingPercentage = 0.25; //percentage of each user's preferences to use to produce recommendations		
    	
    	//Algoritmos de Filtragem Colaborativa Clássicos
		//FCClassic.PearsonCorrelation(datasetUserItenRating);
		//FCClassic.SpearmanCorrelation(datasetUserItenRating);
		//FCClassic.EuclideanDistance(datasetUserItenRating);
		//FCClassic.TanimotoCoefficient(datasetUserItenRating);
		//FCClassic.LogLikelihood(datasetUserItenRating);
		
		//Filtragem Colaborativa Com Entrada de Gêneros Implicito
		//FCProposal.PearsonCorrelation(datasetUserItenRating);
		//FCProposal.SpearmanCorrelation(datasetUserItenRating);
		//FCProposal.EuclideanDistance(datasetUserItenRating);
		//FCProposal.TanimotoCoefficient(datasetUserItenRating);
		//FCProposal.LogLikelihood(datasetUserItenRating);
    	
    	//Etapa 0 - FC Tradicional    	
		FCClassic.Evaluation_PearsonCorrelation(datasetUserItenRating, evaluationPercentage, trainingPercentage);
		FCClassic.Evaluation_SpearmanCorrelation(datasetUserItenRating, evaluationPercentage, trainingPercentage);
		FCClassic.Evaluation_EuclideanDistance(datasetUserItenRating, evaluationPercentage, trainingPercentage);
		FCClassic.Evaluation_TanimotoCoefficient(datasetUserItenRating, evaluationPercentage, trainingPercentage);
		FCClassic.Evaluation_LogLikelihood(datasetUserItenRating, evaluationPercentage, trainingPercentage);
		
    	//Etapa 1 - FC Valores Implícitos
    	FCProposal.Evaluation_PearsonCorrelation(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    	FCProposal.Evaluation_SpearmanCorrelation(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    	FCProposal.Evaluation_EuclideanDistance(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    	FCProposal.Evaluation_TanimotoCoefficient(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    	FCProposal.Evaluation_LogLikelihood(datasetUserItenRating, evaluationPercentage, trainingPercentage);    	    	
    }   
}