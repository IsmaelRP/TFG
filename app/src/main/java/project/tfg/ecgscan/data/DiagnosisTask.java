package project.tfg.ecgscan.data;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import project.tfg.ecgscan.base.DiagnosisListener;

public class DiagnosisTask extends AsyncTask<Void, Void, String> {

    private DiagnosisListener listener;
    private Bitmap image;

    public DiagnosisTask(DiagnosisListener listener, Bitmap image) {
        this.listener = listener;
        this.image = image;
    }

    @Override
    protected String doInBackground(Void... voids) {

        // TODO: implementar llamada a función Matlab con this.image

        try {
            Thread.sleep(3000); // espera 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "TODO: implementar diagnóstico en Matlab";
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onDiagnoseComplete(result);
    }
}