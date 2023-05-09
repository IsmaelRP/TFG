close all, clear all, clc;

%   TEST************************************************************

im = imread('../ecg12.jpg');

espaciado  = get_espaciado(im);
vector = getSignal(im);



