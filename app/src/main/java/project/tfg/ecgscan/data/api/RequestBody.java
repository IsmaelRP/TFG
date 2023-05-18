package project.tfg.ecgscan.data.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestBody {

    @SerializedName("nargout")
    private int nargout;

    @SerializedName("rhs")
    private Rhs rhs;

    public RequestBody(int nargout, Rhs rhs) {
        this.nargout = nargout;
        this.rhs = rhs;
    }

    public static class Rhs {
        @SerializedName("mwdata")
        private List<Integer> mwdata;

        @SerializedName("mwsize")
        private int[] mwsize;

        @SerializedName("mwtype")
        private String mwtype;

        public Rhs(List<Integer> mwdata, int[] mwsize, String mwtype) {
            this.mwdata = mwdata;
            this.mwsize = mwsize;
            this.mwtype = mwtype;
        }
    }


}
