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
		
    	String datasetUserItenRating5perc = "data/rating-trainningSet-5%.csv";
		FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating5perc);
		FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating5perc);
		
		//String datasetUserItenRating25perc = "data/rating-trainningSet-25%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating25perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating25perc);
		
		//String datasetUserItenRating50perc = "data/rating-trainningSet-50%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating50perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating50perc);
		
		//String datasetUserItenRating75perc = "data/rating-trainningSet-75%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating75perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating75perc);
		
		//String datasetUserItenRating100perc = "data/rating-trainningSet-100%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating100perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating100perc);
		
    }   
}