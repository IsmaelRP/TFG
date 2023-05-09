function msg = diagPRWave(ecg)

    %% Calcula las dimensiones del intervalo PR para determinar duración correcta
    %   Problema de bloqueo de la conducción eléctrica: > 5 cuadritos ancho/alto
    %   Problema de bloqueo de la conducción eléctrica: < 3 cuadritos ancho/alto


    moda = mode(ecg);

    [~, posR, anchuraR] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 100, 'MinPeakDistance', 100, 'MinPeakWidth', 5);

    % Para visualizar ************************************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 100, 'MinPeakDistance', 100, 'MinPeakWidth', 5);
    %hold on;

    for i=1:length(posR)
        if i < length(posR)
            limpiarHasta = round((posR(i+1) - posR(i)) / 2) + 10;
        end

        ecg( posR(i) - round(anchuraR(i)) + 0 : posR(i) + limpiarHasta ) = moda - 50;
        %plot(ecg, 'r');

    end
    
    [~, posP, anchuraP] = findpeaks(ecg, 'MinPeakDistance', 10, 'MaxPeakWidth', 120, 'MinPeakWidth', 5, 'MinPeakProminence', 5);

    % Para visualizar ****************************************
    %findpeaks(smoothdata(ecg, 'movmean', 10), 'MinPeakDistance', 10, 'MaxPeakWidth', 120, 'MinPeakWidth', 5, 'MinPeakProminence', 5);
    %hold off;

    inicioP = posP - (anchuraP / 2);
    inicioR = posR - (anchuraR / 2);

    irregularidadR = std( posR(2:end) - posR(1:end-1) );      %   Irregularidad entre el intervalo de los latidos (ondas R)

    if length(anchuraP) > length(anchuraR)

        msg = "Bloqueo AV de 2º grado Mobitz tipo 1, se omiten complejos QRS.";
    elseif length(anchuraP) < length(anchuraR) && irregularidadR > 50
        msg = "Bloqueo AV de 3º grado, fallo completo de la conducción eléctrica auricular y ventricular.";
    else

        

        if irregularidadR > 50    %   Si late irregularmente
            msg = "Bloqueo AV de 2º grado Mobitz tipo 2, se omiten intervalos PR intermitentemente.";

        else    %   Si late 'rítmicamente' a un intervalo estable
            anchuraPR = (inicioR - inicioP) / 10 + 0.5;
            cont = 0;
            progresion = 0.5;
            for i=1:length(anchuraPR)-1
                if anchuraPR(i+1) > anchuraPR(i) + progresion
                    cont = cont + 1;
                    progresion = progresion * 2;
                end
            end
            if cont >= 3
                msg = "Bloqueo AV de 2º grado Mobitz tipo 1, se prolongan los intervalos PR progresivamente.";
            end
        end
    end
    
    if ~exist('msg', 'var')
        if mean(anchuraPR > 5) > 0.70 || mean(anchuraPR < 3) > 0.70
            msg = "Bloqueo AV de 1º grado debido a intervalo PR anormal.";
        else
            msg = "Intervalo PR normal (no hay bloqueo de conducción eléctrica).";
        end
    end
    
end

