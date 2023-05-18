function msg = getDiagnoseMsg(img)

    vector = getSignal(img);
    
    msg = "- ";
    try
        msg = msg + diagPulseDiff(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar el pulso";
    end

    msg = msg + newline + newline + "- ";

    try
        msg = msg + diagPWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar las ondas P";
    end

    msg = msg + newline + newline + "- ";

    try
        msg = msg + diagQRSWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar los complejos QRS";
    end

    msg = msg + newline + newline + "- ";

    try
        msg = msg + diagPRWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar los segmentos PR";
    end

    msg = msg + newline + newline + "- ";


    try
        msg = msg + diagQTWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar los intervalos QT";
    end

    msg = msg + newline + newline + "- ";

    try
        msg = msg + diagSTWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar los segmentos QT";
    end

    msg = msg + newline + newline + "- ";

    try
        msg = msg + diagTWave(vector);
    catch 
        msg = msg + "No se ha podido diagnosticar las ondas T";
    end

end

