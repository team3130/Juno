package frc.team3130.robot.tantanDrive.Paths;

public class RightTurn extends Path {
    @Override
    public double[][] getLeft() {
        return this.left;
    }

    @Override
    public double[][] getRight() {
        return this.right;
    }

    public double[][] left = new double[][]{
        {0.000553, 0.055338, 20},
        {0.003145, 0.129558, 20},
        {0.009080, 0.296759, 20},
        {0.019632, 0.527601, 20},
        {0.036121, 0.824452, 20},
        {0.059868, 1.187373, 20},
        {0.092197, 1.616453, 20},
        {0.134434, 2.111819, 20},
        {0.187906, 2.673639, 20},
        {0.253288, 3.269102, 20},
        {0.330596, 3.865365, 20},
        {0.419847, 4.462579, 20},
        {0.521065, 5.060900, 20},
        {0.634275, 5.660486, 20},
        {0.759505, 6.261499, 20},
        {0.896787, 6.864107, 20},
        {1.046157, 7.468484, 20},
        {1.207653, 8.074809, 20},
        {1.381318, 8.683267, 20},
        {1.567199, 9.294051, 20},
        {1.765347, 9.907363, 20},
        {1.975815, 10.523412, 20},
        {2.198663, 11.142418, 20},
        {2.433956, 11.764611, 20},
        {2.681760, 12.390231, 20},
        {2.942151, 13.019530, 20},
        {3.215206, 13.652775, 20},
        {3.501011, 14.290246, 20},
        {3.799656, 14.932235, 20},
        {4.111237, 15.579053, 20},
        {4.435857, 16.231027, 20},
        {4.773627, 16.888500, 20},
        {5.124664, 17.551837, 20},
        {5.489093, 18.221420, 20},
        {5.867046, 18.897653, 20},
        {6.258665, 19.580960, 20},
        {6.664101, 20.271790, 20},
        {7.083513, 20.970613, 20},
        {7.517071, 21.677923, 20},
        {7.964956, 22.394237, 20},
        {8.427358, 23.120098, 20},
        {8.904479, 23.856070, 20},
        {9.396534, 24.602737, 20},
        {9.903748, 25.360707, 20},
        {10.426360, 26.130602, 20},
        {10.964622, 26.913060, 20},
        {11.518796, 27.708724, 20},
        {12.089161, 28.518242, 20},
        {12.676006, 29.342252, 20},
        {13.279633, 30.181377, 20},
        {13.900358, 31.036205, 20},
        {14.538503, 31.907276, 20},
        {15.194404, 32.795062, 20},
        {15.868403, 33.699940, 20},
        {16.560846, 34.622162, 20},
        {17.272083, 35.561823, 20},
        {18.002459, 36.518818, 20},
        {18.752315, 37.492797, 20},
        {19.521977, 38.483115, 20},
        {20.311753, 39.488776, 20},
        {21.121920, 40.508369, 20},
        {21.952721, 41.540012, 20},
        {22.804346, 42.581287, 20},
        {23.676930, 43.629187, 20},
        {24.570531, 44.680069, 20},
        {25.485124, 45.729625, 20},
        {26.419747, 46.731173, 20},
        {27.372472, 47.636204, 20},
        {28.341211, 48.436974, 20},
        {29.323729, 49.125910, 20},
        {30.317648, 49.695934, 20},
        {31.320464, 50.140789, 20},
        {32.329571, 50.455362, 20},
        {33.342290, 50.635963, 20},
        {34.355111, 50.641065, 20},
        {35.364523, 50.470595, 20},
        {36.367842, 50.165949, 20},
        {37.362456, 49.730699, 20},
        {38.345853, 49.169860, 20},
        {39.315646, 48.489610, 20},
        {40.269585, 47.696979, 20},
        {41.205575, 46.799512, 20},
        {42.121674, 45.804947, 20},
        {43.016850, 44.758789, 20},
        {43.891008, 43.707865, 20},
        {44.744200, 42.659619, 20},
        {45.576555, 41.617744, 20},
        {46.388261, 40.585294, 20},
        {47.179555, 39.564730, 20},
        {47.950715, 38.557972, 20},
        {48.702044, 37.566463, 20},
        {49.433869, 36.591230, 20},
        {50.146527, 35.632943, 20},
        {50.840367, 34.691972, 20},
        {51.515736, 33.768437, 20},
        {52.172981, 32.862260, 20},
        {52.812445, 31.973197, 20},
        {53.434462, 31.100881, 20},
        {54.039359, 30.244846, 20},
        {54.627450, 29.404556, 20},
        {55.199039, 28.579426, 20},
        {55.754416, 27.768836, 20},
        {56.293859, 26.972148, 20},
        {56.817633, 26.188715, 20},
        {57.325991, 25.417893, 20},
        {57.819172, 24.659043, 20},
        {58.297402, 23.911542, 20},
        {58.760898, 23.174783, 20},
        {59.209862, 22.448177, 20},
        {59.644485, 21.731158, 20},
        {60.064949, 21.023185, 20},
        {60.471423, 20.323737, 20},
        {60.864070, 19.632318, 20},
        {61.243039, 18.948457, 20},
        {61.608473, 18.271703, 20},
        {61.960505, 17.601630, 20},
        {62.299262, 16.937833, 20},
        {62.624861, 16.279927, 20},
        {62.937412, 15.627547, 20},
        {63.237019, 14.980349, 20},
        {63.523779, 14.338004, 20},
        {63.797783, 13.700200, 20},
        {64.059115, 13.066644, 20},
        {64.307857, 12.437053, 20},
        {64.544080, 11.811161, 20},
        {64.767854, 11.188716, 20},
        {64.979244, 10.569475, 20},
        {65.178308, 9.953207, 20},
        {65.365102, 9.339693, 20},
        {65.539676, 8.728722, 20},
        {65.702078, 8.120092, 20},
        {65.852350, 7.513610, 20},
        {65.990532, 6.909089, 20},
        {66.116659, 6.306350, 20},
        {66.230763, 5.705219, 20},
        {66.332874, 5.105528, 20},
        {66.423016, 4.507113, 20},
        {66.501212, 3.909817, 20},
        {66.567482, 3.313484, 20},
        {66.621841, 2.717962, 20},
        {66.664914, 2.153638, 20},
        {66.697981, 1.653327, 20},
        {66.722367, 1.219314, 20},
        {66.739396, 0.851469, 20},
        {66.750390, 0.549700, 20},
        {66.756669, 0.313944, 20},
        {66.759552, 0.144163, 20},
        {66.760359, 0.040335, 20},
        {66.760408, 0.002454, 20},
        {66.760408, 0.000000, 20},
    };

    public double[][] right = new double[][]{
        {0.000553, 0.055338, 20},
        {0.002296, 0.087130, 20},
        {0.006322, 0.201280, 20},
        {0.013478, 0.357801, 20},
        {0.024657, 0.558989, 20},
        {0.040753, 0.804782, 20},
        {0.062655, 1.095091, 20},
        {0.091251, 1.429790, 20},
        {0.127425, 1.808709, 20},
        {0.171611, 2.209323, 20},
        {0.223794, 2.609138, 20},
        {0.283954, 3.008000, 20},
        {0.352069, 3.405757, 20},
        {0.428114, 3.802248, 20},
        {0.512060, 4.197312, 20},
        {0.603876, 4.590781, 20},
        {0.703526, 4.982481, 20},
        {0.810970, 5.372233, 20},
        {0.926167, 5.759851, 20},
        {1.049070, 6.145144, 20},
        {1.179628, 6.527908, 20},
        {1.317787, 6.907935, 20},
        {1.463487, 7.285005, 20},
        {1.616665, 7.658888, 20},
        {1.777252, 8.029343, 20},
        {1.945174, 8.396119, 20},
        {2.120353, 8.758949, 20},
        {2.302704, 9.117553, 20},
        {2.492137, 9.471638, 20},
        {2.688555, 9.820894, 20},
        {2.891855, 10.164993, 20},
        {3.101927, 10.503592, 20},
        {3.318653, 10.836328, 20},
        {3.541909, 11.162816, 20},
        {3.771562, 11.482655, 20},
        {4.007471, 11.795417, 20},
        {4.249484, 12.100657, 20},
        {4.497442, 12.397902, 20},
        {4.751175, 12.686659, 20},
        {5.010503, 12.966410, 20},
        {5.275236, 13.236614, 20},
        {5.545170, 13.496706, 20},
        {5.820092, 13.746100, 20},
        {6.099776, 13.984189, 20},
        {6.383983, 14.210351, 20},
        {6.672462, 14.423948, 20},
        {6.964948, 14.624336, 20},
        {7.261166, 14.810868, 20},
        {7.560824, 14.982903, 20},
        {7.863620, 15.139821, 20},
        {8.169241, 15.281031, 20},
        {8.477361, 15.405994, 20},
        {8.787645, 15.514236, 20},
        {9.099753, 15.605381, 20},
        {9.413337, 15.679177, 20},
        {9.728047, 15.735527, 20},
        {10.043538, 15.774536, 20},
        {10.359469, 15.796553, 20},
        {10.675513, 15.802222, 20},
        {10.991364, 15.792541, 20},
        {11.306742, 15.768918, 20},
        {11.621407, 15.733236, 20},
        {11.935165, 15.687912, 20},
        {12.247884, 15.635954, 20},
        {12.559505, 15.581005, 20},
        {12.870052, 15.527373, 20},
        {13.179380, 15.466409, 20},
        {13.487206, 15.391294, 20},
        {13.793402, 15.309774, 20},
        {14.097990, 15.229426, 20},
        {14.401137, 15.157333, 20},
        {14.703132, 15.099755, 20},
        {15.004368, 15.061809, 20},
        {15.305312, 15.047185, 20},
        {15.606236, 15.046202, 20},
        {15.907414, 15.058930, 20},
        {16.209318, 15.095187, 20},
        {16.512346, 15.151397, 20},
        {16.816797, 15.222542, 20},
        {17.122846, 15.302441, 20},
        {17.430527, 15.384060, 20},
        {17.739724, 15.459850, 20},
        {18.050165, 15.522069, 20},
        {18.361694, 15.576421, 20},
        {18.674322, 15.631414, 20},
        {18.987996, 15.683719, 20},
        {19.302589, 15.729643, 20},
        {19.617912, 15.766132, 20},
        {19.933726, 15.790727, 20},
        {20.249757, 15.801507, 20},
        {20.565697, 15.797029, 20},
        {20.881222, 15.776266, 20},
        {21.195993, 15.738550, 20},
        {21.509664, 15.683511, 20},
        {21.821884, 15.611028, 20},
        {22.132308, 15.521183, 20},
        {22.440592, 15.414217, 20},
        {22.746402, 15.290500, 20},
        {23.049412, 15.150498, 20},
        {23.349307, 14.994745, 20},
        {23.645784, 14.823830, 20},
        {23.938551, 14.638371, 20},
        {24.227331, 14.439007, 20},
        {24.511859, 14.226385, 20},
        {24.791882, 14.001150, 20},
        {25.067161, 13.763941, 20},
        {25.337468, 13.515380, 20},
        {25.602590, 13.256077, 20},
        {25.862322, 12.986619, 20},
        {26.116474, 12.707571, 20},
        {26.364863, 12.419478, 20},
        {26.607320, 12.122858, 20},
        {26.843685, 11.818207, 20},
        {27.073805, 11.505999, 20},
        {27.297538, 11.186682, 20},
        {27.514752, 10.860683, 20},
        {27.725320, 10.528408, 20},
        {27.929125, 10.190241, 20},
        {28.126056, 9.846547, 20},
        {28.316009, 9.497672, 20},
        {28.498888, 9.143943, 20},
        {28.674602, 8.785672, 20},
        {28.843065, 8.423154, 20},
        {29.004198, 8.056670, 20},
        {29.157928, 7.686485, 20},
        {29.304185, 7.312855, 20},
        {29.442905, 6.936021, 20},
        {29.574029, 6.556212, 20},
        {29.697502, 6.173650, 20},
        {29.813273, 5.788545, 20},
        {29.921295, 5.401098, 20},
        {30.021525, 5.011503, 20},
        {30.113924, 4.619948, 20},
        {30.198457, 4.226610, 20},
        {30.275090, 3.831664, 20},
        {30.343795, 3.435278, 20},
        {30.404548, 3.037615, 20},
        {30.457324, 2.638834, 20},
        {30.502106, 2.239090, 20},
        {30.538877, 1.838535, 20},
        {30.568037, 1.457999, 20},
        {30.590437, 1.120007, 20},
        {30.606965, 0.826393, 20},
        {30.618511, 0.577285, 20},
        {30.625966, 0.372777, 20},
        {30.630225, 0.212930, 20},
        {30.632180, 0.097785, 20},
        {30.632728, 0.027360, 20},
        {30.632761, 0.001665, 20},
        {30.632761, 0.000000, 20},
    };
}