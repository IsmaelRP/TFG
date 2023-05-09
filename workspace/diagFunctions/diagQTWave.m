function msg = diagQTWave(ecg)

    %% Calcula las dimensiones del intervalo QT para determinar duración correcta
    %   Problema de riesgo de arrítmias ventriculares: > 11 cuadritos ancho


    moda = mode(ecg);

    [~, posR, anchuraR] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);

    % Para visualizar ************************************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);
    %hold on;

    for i=length(posR):-1:1
        if i > 1
            limpiarHasta = round((posR(i) - posR(i-1)) / 2);
        else
            limpiarHasta = posR(i)-1;
        end

        ecg( posR(i) - limpiarHasta : posR(i) + round(anchuraR(i)) + 3 ) = moda - 5;
        %plot(ecg, 'r');

    end
    
    [~, posT, anchuraT] = findpeaks(ecg, 'MinPeakDistance', 100, 'MinPeakWidth', 5);

    % Para visualizar ****************************************
    %findpeaks(ecg, 'MinPeakDistance', 100, 'MinPeakWidth', 5);
    %hold off;

    inicioT = posT - (anchuraT / 2);
    inicioR = posR - (anchuraR / 2);

    anchuraQT = (inicioT - inicioR) / 10 + 4.5;
    anchuraQT = anchuraQT(anchuraQT > 0);

    if mean(anchuraQT > 11) > 0.85
        msg = "Posible problema de riesgo de arritmia ventricular debido a intervalo QT anormal.";
    else
        msg = "Intervalo QT normal (riesgo de arritmia ventricular).";
    end

end

