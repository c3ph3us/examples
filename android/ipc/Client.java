  
  
  public static void test(final Context context) {
        boolean b = bindIfStarted(context, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // do magic :)
                TestService.IStub iStub = TestService.Stub.asInterface(service);
                try {
                    // test read from server via stub
                    iStub.getString();
                    // test send server via stub
                    iStub.setString("CLIENT HELLO");
                    //
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // or do unbind right after we are connected?
                    unbind(context,this);
                }
            }
             
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("AIT/D", name.getClassName());
            }
        }, new ComponentName(context.getPackageName(), TestService.class.getName()));
    }
    
    
    protected static void unbind(Context context, ServiceConnection serviceConnection) {
        // check on context
        if (context != null
                && serviceConnection != null) {
            // unbind
            context.unbindService(serviceConnection);
        }
    }
