import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
 * adapter with re-usable placeholder example 
 * made by @ceph3us
 * https://github.com/c3ph3us/examples/VaultListAdapter.java
 */
public class VaultListAdapter extends BaseAdapter {

    /** list holding adapter items */
    private List<VaultItem> _vaultItemList;
    
    public VaultListAdapter(){
        /** initiate list with new empty ArrayList
         * if class instance is created without any parameter  */
        _vaultItemList = new ArrayList();
    }

    /** constructor for adapter */
    public VaultListAdapter(List<VaultItem> vaultItemList) {
        _vaultItemList = vaultItemList;
    }
   
   /** to add item to our list */
    public void addVaultItem(VaultItem vaultItem) {
        _vaultItemList.add(vaultItem);
    }
    
    @Override
    public int getCount() {
        return _vaultItemList.size();
    }

    @Override
    public VaultItem getItem(int position) {
        return _vaultItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /** don't operate on method parameter create new variable */
        View view = convertView;

        /** get your item at position */
        VaultItem vaultItem = getItem(position);

        /** get example data from item
         * voucher code unique? so we will use it as view tag */
        String voucherCode = vaultItem.getVoucherCode();

        /** check if we not get view */
        if (view == null) {

            /** get context from parent */
            Context context = parent.getContext();

            /** inflate child view from xml */
            view = LayoutInflater.from(context).inflate(R.layout.vault_item, null, parent);


        } else {

            /** get tag from given view */
            String viewTag = (String) view.getTag();

            /** if tag match our voucher code return given view */
            if (viewTag != null && viewTag.equals(voucherCode)) {
                return view;
            }
        }

        /** if not or view is newly inflated create convert view */
        ViewHolder viewholder = new ViewHolder(view);

        /**  set data to view */
        viewholder.tvMerchantName.setText(vaultItem.getMerchantNAme());

        Drawable voucherImage = createBarCode(voucherCode);/** get it from as you want */
        viewholder.ivBarCode.setImageDrawable(voucherImage);


        /** set tag to new view */
        view.setTag(voucherCode);

        /** return view */
        return view;
    }

    /** static cache item view */
    static class ViewHolder {

        TextView tvMerchantName,
                 tvVoucherCode;

        ImageView ivBarCode;

        /** holder constructor */
        public ViewHolder(View view) {

            tvVoucherCode = view.findViewById(R.id.tvVoucherCode);
            tvMerchantName = view.findViewById(R.id.tvMerchantNAme);

            ivBarCode = view.findViewById(R.id.ivBarCode);
        }

    }
}

    /*
     * example of vault item class 
     * should put outside this class as separate class data object 
     */
    public class VaultItem {

        /** unique? so we coud use it as view tag */
        private String _voucherCode;
        private String _merchantName;

        public String getVoucherCode() {
            return _voucherCode;
        }

        public String getMerchantNAme() {
            return _merchantName;
        }

        public void setMerchantName(String merchantName){
            _merchantName = merchantName;
        }
    }
