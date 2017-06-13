package lapesd.sacenti.recommender.recommender_mc;

public class App
{
	public static void main( String[] args )
    {
		System.out.println("5%");
    	String datasetUserItenRating5perc = "data/Testes/rating-trainningSet-5%NOVO.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating5perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating5perc);		
		//FCClassic.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating5perc);
    	//FCProposal.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating5perc);
    	//FCClassic.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating5perc);
    	FCProposal.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating5perc);
		
		System.out.println("25%");
    	String datasetUserItenRating25perc = "data/Testes/rating-trainningSet-25%NOVO.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating25perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating25perc);
		//FCClassic.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating25perc);
		//FCProposal.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating25perc);
    	//FCClassic.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating25perc);
    	FCProposal.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating25perc);
				
		System.out.println("50%");
    	String datasetUserItenRating50perc = "data/rating-trainningSet-50%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating50perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating50perc);
		//FCClassic.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating50perc);
		//FCProposal.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating50perc);
    	//FCClassic.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating50perc);
    	FCProposal.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating50perc);
		
		System.out.println("75%");
    	String datasetUserItenRating75perc = "data/rating-trainningSet-75%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating75perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating75perc);
		//FCClassic.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating75perc);
		//FCProposal.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating75perc);
    	//FCClassic.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating75perc);
    	FCProposal.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating75perc);
		
		System.out.println("100%");
    	String datasetUserItenRating100perc = "data/rating-trainningSet-100%.csv";
		//FCClassic.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating100perc);
		//FCProposal.Webmedia_Evaluation_PearsonCorrelation(datasetUserItenRating100perc);
		//FCClassic.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating100perc);
		//FCProposal.Webmedia_TimeAndAverageNeighbors_PearsonCorrelation(datasetUserItenRating100perc);
    	//FCClassic.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating100perc);
    	FCProposal.Webmedia_numberUsersCriterionNeighbordhood(datasetUserItenRating100perc);
		
    }   
}