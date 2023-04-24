package com.miniproject.football.Service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Service;


@Service
public class PredictMachine {

    private static final String CSV_FILE_PATH = "./football.csv";
    private static final int NUM_INPUTS = 4;
    private static final int NUM_OUTPUTS = 2;

    private MultiLayerNetwork model;
    private Map<String, Double[]> teamScores;
    private NormalizerMinMaxScaler normalizer;
    private DataSet dataSet;
    

    String modelDirectory = "C:/Users/snow_/Desktop/Mini ProjectTwo/predict";
    // PredictMachine model2 = new PredictMachine();

    public PredictMachine() throws IOException, InterruptedException {
        Nd4j.setDefaultDataTypes(DataType.FLOAT, DataType.FLOAT);
        Nd4j.getRandom().setSeed(1234);

        
        dataSet = loadData();
        buildModel();
        trainModel(100, 0.001, dataSet);
    }

    public class RandomUtil {
        public static long generateSeed() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] seedBytes = secureRandom.generateSeed(Long.BYTES);
            return java.nio.ByteBuffer.wrap(seedBytes).getLong();
        }
    }

    private DataSet loadData() {
        DataSet dataSet = null;

        //read csv, skip first row, split by ,
        try (RecordReader recordReader = new CSVRecordReader(1, ',')) {
            recordReader.initialize(new FileSplit(new File(CSV_FILE_PATH)));
    
            teamScores = new HashMap<>();

            while (recordReader.hasNext()) {
                List<Writable> record = recordReader.next();
            
                String homeTeam = record.get(0).toString();
                String awayTeam = record.get(1).toString();
                int homeScore = Integer.parseInt(record.get(2).toString());
                int awayScore = Integer.parseInt(record.get(3).toString());

                Double[] homeForm = teamScores.getOrDefault(homeTeam, new Double[]{0.0, 0.0, 0.0, 0.0, 0.0});
                homeForm[0] += 1; // increment the number of matches played
                homeForm[1] += homeScore; // add the home team's goals scored
                homeForm[2] += (10- awayScore); // add the home team's goals conceded
                homeForm[3] = homeForm[1] / homeForm[0]; // calculate the home team's average goals scored
                homeForm[4] = homeForm[2] / homeForm[0]; // calculate the home team's average goals conceded
                teamScores.put(homeTeam, homeForm);
            
                Double[] awayForm = teamScores.getOrDefault(awayTeam, new Double[]{0.0, 0.0, 0.0, 0.0, 0.0});
                awayForm[0] += 1; // increment the number of matches played
                awayForm[1] += awayScore; // add the away team's goals scored
                awayForm[2] += (10-homeScore); // add the away team's goals conceded
                awayForm[3] = awayForm[1] / awayForm[0]; // calculate the away team's average goals scored
                awayForm[4] = awayForm[2] / awayForm[0]; // calculate the away team's average goals conceded
                teamScores.put(awayTeam, awayForm);
                

            }

                //create INDArray, input data to features(input) and labels(target)
                INDArray features = Nd4j.create(teamScores.size(), 4);
                INDArray labels = Nd4j.create(teamScores.size(), 2);

                int i = 0;
                for (Map.Entry<String, Double[]> entry : teamScores.entrySet()) {
                    Double[] scores = entry.getValue();
                    features.putRow(i, Nd4j.create(new double[]{scores[1], scores[2], scores[3], scores[4]}));
                    labels.putRow(i, Nd4j.create(new double[]{scores[1], scores[2]}));
                    //labels.putRow(i+1, Nd4j.create(new double[]{scores[2], scores[4]}));
                    i++;
                }

                dataSet = new DataSet(features, labels);
                //System.out.println("-----------------------------------Features before normalization:");
                //System.out.println(dataSet.getFeatures());
                //System.out.println("Labels before normalization:");
                //System.out.println(dataSet.getLabels());

                // scalar normalizer to be within default range of 0 to 1
                // NormalizerMinMaxScaler normalizer = new NormalizerMinMaxScaler(0, 10); this will scale between 0-10 if needed
                //normalizer = new NormalizerMinMaxScaler(0, 3);
                normalizer = new NormalizerMinMaxScaler();
                // fit calculates normalization parameters(min and max value of each feature) based on given input 
                normalizer.fit(dataSet);
                // Normalize the data set
                normalizer.transform(dataSet);
                DataSetIterator iterator = new ListDataSetIterator<>(dataSet.asList());
                while (iterator.hasNext()) {
                    dataSet = iterator.next();
                    System.out.println("Features2: " + dataSet.getFeatures());
                    System.out.println("Labels2: " + dataSet.getLabels() + "i'm here to see what it is iterating");
                }
                

                // Print out the features and labels after normalizing
                //System.out.println("Features after normalization:");
                // System.out.println(dataSet.getFeatures());
                // System.out.println("Labels after normalization:");
                // System.out.println(dataSet.getLabels());

                } catch (Exception e) {
                    throw new RuntimeException("Error loading data", e);
                }
            return dataSet;
        }
    
    private void buildModel() {

                // Define the output layer with linear activation function
                OutputLayer outputLayer = new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nIn(10)
                        .nOut(NUM_OUTPUTS)
                        .activation(Activation.IDENTITY) // Use linear activation function for regression
                        .build();
            
                // Define the neural network architecture
                MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                        .seed(RandomUtil.generateSeed())
                        .updater(new Adam())
                        .list()
                        .layer(new DenseLayer.Builder()
                                .nIn(NUM_INPUTS)
                                .nOut(10)
                                .activation(Activation.TANH)
                                .build())
                        .layer(outputLayer) // Add output layer
                        .build();
            
                // Create the neural network
                model = new MultiLayerNetwork(config);
                model.init();
        
        }

        public void trainModel(int numEpochs, double learningRate, DataSet dataSet) throws IOException {
            // Define the regression evaluation
            RegressionEvaluation eval = new RegressionEvaluation();
        
            // Train the model
            for (int i = 0; i < numEpochs; i++) {
                model.fit(dataSet);
                eval.eval(dataSet.getLabels(), model.output(dataSet.getFeatures()));
            }
        
            // Print the evaluation metrics
            System.out.println(eval.stats());
        }
    
    

        public int[] predict(String homeTeam, String awayTeam) {
                    // Create an array to store the predicted scores
                    int[] scores = new int[2];
                
                    // Prepare the input features for the home and away teams
                    INDArray homeFeatures = Nd4j.create(new double[][] {{ teamScores.get(homeTeam)[1], teamScores.get(homeTeam)[2], teamScores.get(homeTeam)[3], teamScores.get(homeTeam)[4]}});
                    INDArray awayFeatures = Nd4j.create(new double[][] {{ teamScores.get(awayTeam)[1], teamScores.get(awayTeam)[2], teamScores.get(awayTeam)[3], teamScores.get(awayTeam)[4]}});
                    System.out.println("Home features:------------------------------------ " + homeFeatures.toString());
                    System.out.println("Away features:------------------------------------ " + awayFeatures.toString());

                
                    INDArray homePredictedOutputs = model.output(homeFeatures);
                    normalizer.revertLabels(homePredictedOutputs);
                    double homeResult = homePredictedOutputs.getDouble(0);
                    System.out.println(homeResult);

                    INDArray awayPredictedOutputs = model.output(awayFeatures);
                    normalizer.revertLabels(awayPredictedOutputs);
                    double awayResult = awayPredictedOutputs.getDouble(1);
                    System.out.println(awayResult);

                    // Extra stuff to affect scores
                        // Call xGoalService to retrieve xG and xGA values for selected teams----------------------------------
                        xGoalService xgService = new xGoalService();
                        xgService.xGoalServices(homeTeam, awayTeam);
                        
                        // Access the xG and xGA values for the home and away teams
                        double homeTeamXG = xgService.getHomeTeamXGavg();
                        double awayTeamXG = xgService.getAwayTeamXGavg();

                        // Call LuckService to get the luck factor-------------------------------------------------
                        double luckFactor = LuckService.luckFactor();
                        double luckFactor2 = LuckService.luckFactor2();

                        // Call HomeGround Advantage Service-----------------------------------------
                        double homeGrdAdv = HomeGrdAdvService.homeGroAdv(homeTeam);

                        double homeFinal= homeResult + (luckFactor * homeGrdAdv * homeTeamXG)-0.1;
                        double awayFinal = awayResult + (luckFactor2 * awayTeamXG)-0.1;
                        System.out.println("Home team score: " + homeResult + " Home team xG: " + homeTeamXG + " Luck factor: " + luckFactor + " Home ground advantage: " + homeGrdAdv);
                        System.out.println("Away team score: " + awayResult + " away team xG: " + awayTeamXG + " Luck factor2: " + luckFactor2);


                    double homeClampResult = (Math.min(Math.max(homeFinal, 0.0), 10));
                    double awayClampResult = (Math.min(Math.max(awayFinal, 0.0), 10));
                
                    // Store the predicted scores as integers
                    scores[0] = (int) Math.floor(homeClampResult);
                    scores[1] = (int) Math.floor(awayClampResult);
                
                    return scores;
                }
  
}
//Key - Value
//Key - team names
//value 1 - matches played
//value 2 - total home+away goals
//value 3 - total home+away goals conceded
//value 4 - average goals scored per match(home and away)
//value 5 - average goals conceded(home and away)