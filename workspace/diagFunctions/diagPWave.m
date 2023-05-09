function msg = diagPWave(ecg)

    %% Calcula las dimensiones de la onda P para determinar amplitud y duración correcta
    %   Problema de conducción eléctrica: > 2.5 cuadritos ancho/alto


    moda = mode(ecg);

    [~, pos, anchura] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);

    % Para visualizar ************************************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);
    %hold on;

    for i=1:length(pos)
        if i < length(pos)
            limpiarHasta = round((pos(i+1) - pos(i)) / 2);
        else
            limpiarHasta = length(ecg);
        end

        ecg( pos(i) - round(anchura(i)) + 3 : pos(i) + limpiarHasta ) = moda - 5;
        %plot(ecg, 'r');

    end
    
    [tam, posP, anchura] = findpeaks(ecg, 'MinPeakDistance', 100, 'MinPeakWidth', 5);

    % Para visualizar ****************************************
    %findpeaks(ecg, 'MinPeakDistance', 100, 'MinPeakWidth', 5);
    %hold off;

    tam = tam / 60;
    anchura = anchura / 10;

    distancias = (posP(2:end) - posP(1:end-1)) / 10;

    msg = "";
    if length(tam) ~= length(posP) && std(distancias) > 1
        msg = "Cierta inactividad auricular, posible fibrilación auricular, ";
    elseif std(distancias) > 1
        msg = "Ritmo irregular, posible fibrilación auricular, ";
    end

    if mean(tam > 2.5) > 0.85 || mean(anchura > 2.5) > 0.85
        msg = msg + "Posible problema de conducción eléctrica debido a onda P anormal.";
    else
        msg = msg + "Onda P normal (conducción eléctrica).";
    end
    
end

