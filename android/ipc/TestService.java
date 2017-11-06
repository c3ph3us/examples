package pl.ceph3us.projects.android.common.services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

/**
 * Created by ceph3us on 06.11.17.
 * file belong to pl.ceph3us.projects.android.common.services
 */
public class TestService extends Service {

    private static final String T = "TR";
    private static final String DESCRIPTOR = "fuckYouGoogle";

    @Override
    public IBinder onBind(Intent intent) {
            /* server implementation */
            return new Binder() {

                {
                    attachInterface(new Stub(this), DESCRIPTOR);
                }

                @Override
                protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                    switch (code) {
                        case 1:
                            String serverString = "SERVER HELLO!";
                            reply.writeString(serverString);
                            Log.i(T, "onTransact server sends: "+serverString);
                            break;
                        case 2:
                            String s = data.readString();
                            Log.i(T, "onTransact server received: "+s);
                            break;
                    }
                    return true;
                }
            };
    }

    public interface IStub extends IInterface {
        String getString() throws RemoteException;

        void setString(String s) throws RemoteException;
    }

    /**
     * client implementation Stub
     */
    public static class Stub extends Binder implements IStub {

        private IBinder _remote;

        public Stub(IBinder binder) {
            _remote = binder;
        }

        public static IStub asInterface(IBinder binder) {
            //
            IInterface dd = binder != null
                    ? binder.queryLocalInterface(DESCRIPTOR)
                    : null;
            //
            return dd != null
                    && IStub.class.isAssignableFrom(dd.getClass())
                    ? (IStub) dd
                    : null;
        }

        @Override
        public String getString() throws RemoteException {
            Parcel in = Parcel.obtain();
            Parcel out = Parcel.obtain();
            _remote.transact(1, in, out, 0);
            String s = out.readString();
            Log.i(T, "onTransact client received: "+s);
            return s;
        }

        @Override
        public void setString(String s) throws RemoteException {
            Log.i(T, "onTransact client sends: "+s);
            Parcel in = Parcel.obtain();
            Parcel out = Parcel.obtain();
            in.writeString(s);
            _remote.transact(2, in, out, 0);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    }
}
