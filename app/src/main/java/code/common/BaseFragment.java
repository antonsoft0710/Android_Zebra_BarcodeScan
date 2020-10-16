package code.common;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    protected Activity mActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }
}
