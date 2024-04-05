import librosa
import numpy as np
from sklearn.cluster import DBSCAN
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from pydub import AudioSegment
from pydub.silence import split_on_silence
import statistics
import os


# choix du fichier 

voix = input("choix du fichier :")

# Load the audio files
audio_files = [('nabil.wav', 'nabil'), ('hani.wav', 'hani'), ('kamel.wav', 'kamel')]

# initialisation 
mfcc_features = []
labels = []

for audio_file, speaker_name in audio_files:
    audio = AudioSegment.from_file(audio_file)

    # on devise les fichier audio parceque y en a ils sont trop lent 
    segments = split_on_silence(audio, min_silence_len=1000, silence_thresh=-30)

    for j, segment in enumerate(segments):
        # Extract the array of samples from the AudioSegment object and convert it to a numpy array
        samples = np.array(segment.get_array_of_samples())

        # Convert the array of samples to a floating-point data type
        samples = samples.astype(float)

        # Compute the MFCC features using the numpy array of samples
        features = librosa.feature.mfcc(y=samples, sr=segment.frame_rate)

        # Add the MFCC features to the list of features
        mfcc_features.extend(features.T)

        # Add the speaker's label to the list of labels
        labels.extend([speaker_name] * features.shape[1])

# Split the MFCC features and labels into a training set and a test set
X_train, X_test, y_train, y_test = train_test_split(mfcc_features, labels, test_size=0.2)

# Scale the training and test sets
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Train a classifier on the scaled training set
clf = SVC()
clf.fit(X_train_scaled, y_train)

# Test the classifier on the scaled test set
accuracy = clf.score(X_test_scaled, y_test)
print(f'Test accuracy: {accuracy:.2f}')

# Load the third audio file
audio_file = voix
audio = AudioSegment.from_file(audio_file)

# Split the audio into segments based on silence
segments = split_on_silence(audio, min_silence_len=1000, silence_thresh=-30)

for j, segment in enumerate(segments):
    # Extract the array of samples from the AudioSegment object and convert it to a numpy array
    samples = np.array(segment.get_array_of_samples())
    
    # Convert the array of samples to a floating-point data type
    samples = samples.astype(float)

    # Compute the MFCC features using the numpy array of samples
    features = librosa.feature.mfcc(y=samples, sr=segment.frame_rate)

    # Scale the MFCC features
    features_scaled = scaler.transform(features.T)

# Predict the speaker's name for each MFCC feature in the segment
predictions = clf.predict(features_scaled)

# Identify the speaker as the most common prediction
speaker_label = statistics.mode(predictions)

# Print the predicted speaker's name
print(f'Speaker in segment {j} of {audio_file}: {speaker_label}')
