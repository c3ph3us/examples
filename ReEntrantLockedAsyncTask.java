import android.os.AsyncTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * re-entrant lock example in async task await/signall on lock condition
 * made by @ceph3us
 * https://github.com/c3ph3us/examples/ReEntrantLockedAsyncTask.java
 */
public class ReEntrantLockedAsyncTask extends AsyncTask<Void, Long, Void> {

    /**
     * flag for while() loop to break it on given condition
     */
    private volatile boolean isDataStillNeeded = true;//mutex for data

    /**
     * re-entrant lock
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * condition for lock
     */
    private final Condition dataAcquired = lock.newCondition();

    /**
     * flag to hold lock state
     */
    private boolean isLockAcquired;


    @Override
    protected Void doInBackground(Void... params) {

        /**
         *  example temporary placeholder
         * for gathered data
         * */
        long someProgressData = 0;

        try {

            /**
             * try get interruptibility lock
             * to be able to call await on condition
             * */
            lock.lockInterruptibly();

            /**
             * create while() loop
             * to gather data
             * loop will be broken by
             * changing given flag state
             */
            while (isDataStillNeeded) {

                /** get lock state */
                isLockAcquired = lock.isHeldByCurrentThread();

                /** check for lock state */
                if (isLockAcquired) {

                    /**
                     * example gathered data
                     * increase data counter
                     * */
                    someProgressData++;

                    /** publish gathered result */
                    publishProgress(someProgressData);

                    /**
                     * call await on condition
                     * for given lock after lock was acquired
                     * */
                    dataAcquired.await();
                }
            }
            
        } catch (InterruptedException e) {

            /**
             * her we catch
             * lock interrupt exception
             */
            e.printStackTrace();

        } finally {

            /**
             * this block is proceed
             * after interrupt exception occurred
             * or task was finish by normal flow
             * after flag state was set to false
             * */

            /**
             * unlock lock
             * to be able to finish task
             * */
            lock.unlock();
        }

        /** return from task */
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... resultList) {

        /**
         *  example do some work witch check result
         *  & change while loop flag state
         * */
        if (resultList[0] > 1000) {

            /**
             *  we don't need more data
             * change flat state
             * */
            isDataStillNeeded = false;
        }

        /**
         *  call method which will call
         * condition signal to wake-up thread
         * */
        retry();
    }

    /**
     * on task canceled
     */
    @Override
    protected void onCancelled() {
        /**
         * NOTE Make sure to clean up
         * if the task is killed
         * */
        terminateTask();
    }

    /**
     * the task will only finish
     * when we call this method
     * or by thrown interrupt exception
     */

    private void terminateTask() {
        /** unlock lock */
        lock.unlock();
    }

    /**
     * method to wake-up thread
     * locke by lock.lockInterruptibly()
     * after condition await was called
     * on try {} block in while() loop
     */
    public void retry() {

        /**
         * try get lock
         * for current calling thread
         * */
        lock.lock();

        /** get lock state */
        isLockAcquired = lock.isHeldByCurrentThread();

        /** check if lock was acquired */
        if (isLockAcquired) {

            /**
             * if so
             * send condition signal
             * to wake-up awaiting thread
             * */
            dataAcquired.signal();
        }

        /**
         * unlock thread locked
         * for calling on condition signal
         * */
        lock.unlock();
    }
}
