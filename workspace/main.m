close all, clear all, clc;
dbstop if error

img = imread('../ecg2.jpg');
%img = imread('../bloqueo3.jpg');
%img = imread('../st.jpg');
%img = imread('../t.jpg');

% BORRAR
%espaciado  = get_espaciado(im);
img = imcrop(img);
%subplot(2, 1, 1);
%imshow(img);
% BORRAR

try
    %msg = getDiagnoseMsg(img)
    
    vector = getSignal(img);
    %diagPulseDiff(vector)  % Método 5
    diagPWave(vector)      % Método 1
    %diagQRSWave(vector)    % Método 3
    %diagPRWave(vector)     % Método 2
    %diagQTWave(vector)     % Método 4
    %diagSTWave(vector)     % Método 6
    %diagTWave(vector)      % Método 7

catch 
    "Imagen inválida"
end

