function msg = getDiagnoseMsg(img)

    vector = getSignal(img);

    msg = "- " + diagPulseDiff(vector) + newline + "- " + diagPWave(vector) + newline + "- " + diagQRSWave(vector) + newline + "- " + diagPRWave(vector)  + newline + "- " + diagQTWave(vector) + newline + "- " + diagSTWave(vector) + newline + "- " + diagTWave(vector) + ".";

end

