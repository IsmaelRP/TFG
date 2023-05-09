function msg = diagQRSWave(ecg)

    %% Calcula las dimensiones del complejo QRS para determinar una duración correcta
    %   Problema de conducción eléctrica: > 2.5 cuadritos ancho


    [~, ~, anchura] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);
    
    % Para visualizar *******************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);

    anchura = anchura / 10;
    

    if mean(anchura > 2.5) > 0.85
        msg = "Posible problema de conducción eléctrica debido a complejo QRS anormal en anchura.";
    else
        msg = "Complejo QRS normal (conducción eléctrica).";
    end


end

