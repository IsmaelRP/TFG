function msg = diagSTWave(ecg)
    %% Calcula las dimensiones del intervalo ST para determinar elevación o depresión
    %   Depresión (isquemia miocárdica): < 0.5 cuadritos en al menos 2 intervalos seguidos
    %   Elevación (infarto agudo de miocardio de espesor total): > 1.5 cuadritos en al menos 2 intervalos seguidos

    moda = mode(ecg);

    [~, posR, anchuraR, ~] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);
    
    % Para visualizar *******************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);

    
    desvInicio = 0;
    desvFin = 10;
    desviacionST = zeros(1, length(posR));
    for i=1:length(posR)
        cambioDireccion = ischange( ecg(floor(posR(i) - anchuraR(i) -desvInicio): floor(posR(i) + anchuraR(i) +desvFin)) , 'linear', 'Threshold', 2);

        inicioProminencia = find(cambioDireccion == 1, 1, 'first') + floor(posR(i) - anchuraR(i) -desvInicio) -2;
        finProminencia = find(cambioDireccion == 1, 1, 'last') + floor(posR(i) - anchuraR(i) );

        desviacionST(i) = ecg(inicioProminencia) - ecg(finProminencia);
    end


    msg = "Intervalo ST normal (no hay indicios de irregularidad miocárdica).";
    contDepr = 0;
    contElev = 0;
    for i=1:length(desviacionST)
        if desviacionST > 5.5
            contDepr = contDepr +1;
            contElev = 0;
        elseif desviacionST < -15
            contElev = contElev +1;
            contDepr = 0;
        else
            contElev = 0;
            contDepr = 0;
        end

        if contDepr >= 2
            msg = "Depresión de intervalo ST, posible isquemia miocárdica.";
        elseif contElev >= 2
            msg = "Elevación de intervalo ST, posible infarto agudo de miocardio de espesor total.";
        end

    end

    

end

