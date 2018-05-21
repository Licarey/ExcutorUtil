package com.example.liming.validateproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annotation.LCheckActivity;
import com.example.annotation.LCheckView;

import java.util.Observable;
import java.util.Observer;

/**
 * 处理表单信息输入完全校验
 * 响应式编程是一种基于异步数据流概念的编程模式。数据流就像一条河：它可以被观测，被过滤，被操作，
 * 或者为新的消费者与另外一条流合并为一条新的流。
 * <p>
 * 详见 http://reactivex.io/documentation
 * <p>
 * observable创建操作-------------------------------------------------------------------------------------------------------------------------------------------
 * Create — 通过调用观察者的方法从头创建一个Observable
 * Defer — 在观察者订阅之前不创建这个Observable，为每一个观察者创建一个新的Observable
 * Empty/Never/Throw — 创建行为受限的特殊Observable
 * From — 将其它的对象或数据结构转换为Observable
 * Interval — 创建一个定时发射整数序列的Observable
 * Just — 将对象或者对象集合转换为一个会发射这些对象的Observable
 * Range — 创建发射指定范围的整数序列的Observable
 * Repeat — 创建重复发射特定的数据或数据序列的Observable
 * Start — 创建发射一个函数的返回值的Observable
 * Timer — 创建在一个指定的延迟之后发射单个数据的Observable
 * <p>
 * observable变换操作-------------------------------------------------------------------------------------------------------------------------------------------
 * Buffer — 缓存，可以简单的理解为缓存，它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
 * FlatMap — 扁平映射，将Observable发射的数据变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable，可以认为是一个将嵌套的数据结构展开的过程。
 * GroupBy — 分组，将原来的Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
 * Map — 映射，通过对序列的每一项都应用一个函数变换Observable发射的数据，实质是对序列中的每一项执行一个函数，函数的参数就是这个数据项
 * Scan — 扫描，对Observable发射的每一项数据应用一个函数，然后按顺序依次发射这些值
 * Window — 窗口，定期将来自Observable的数据分拆成一些Observable窗口，然后发射这些窗口，而不是每次发射一项。类似于Buffer，但Buffer发射的是数据，Window发射的是Observable，每一个Observable发射原始Observable的数据的一个子集
 * <p>
 * observable过滤操作-------------------------------------------------------------------------------------------------------------------------------------------
 * Debounce — 只有在空闲了一段时间后才发射数据，通俗的说，就是如果一段时间没有操作，就执行一次操作
 * Distinct — 去重，过滤掉重复数据项
 * ElementAt — 取值，取特定位置的数据项
 * Filter — 过滤，过滤掉没有通过谓词测试的数据项，只发射通过测试的
 * First — 首项，只发射满足条件的第一条数据
 * IgnoreElements — 忽略所有的数据，只保留终止通知(onError或onCompleted)
 * Last — 末项，只发射最后一条数据
 * Sample — 取样，定期发射最新的数据，等于是数据抽样，有的实现里叫ThrottleFirst
 * Skip — 跳过前面的若干项数据
 * SkipLast — 跳过后面的若干项数据
 * Take — 只保留前面的若干项数据
 * TakeLast — 只保留后面的若干项数据
 * <p>
 * observable组合操作-------------------------------------------------------------------------------------------------------------------------------------------
 * And/Then/When — 通过模式(And条件)和计划(Then次序)组合两个或多个Observable发射的数据集
 * CombineLatest — 当两个Observables中的任何一个发射了一个数据时，通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据），然后发射这个函数的结果
 * Join — 无论何时，如果一个Observable发射了一个数据项，只要在另一个Observable发射的数据项定义的时间窗口内，就将两个Observable发射的数据合并发射
 * Merge — 将两个Observable发射的数据组合并成一个
 * StartWith — 在发射原来的Observable的数据序列之前，先发射一个指定的数据序列或数据项
 * Switch — 将一个发射Observable序列的Observable转换为这样一个Observable：它逐个发射那些Observable最近发射的数据
 * Zip — 打包，使用一个指定的函数将多个Observable发射的数据组合在一起，然后将这个函数的结果作为单项数据发射
 * <p>
 * observable错误处理-------------------------------------------------------------------------------------------------------------------------------------------
 * Catch — 捕获，继续序列操作，将错误替换为正常的数据，从onError通知中恢复
 * Retry — 重试，如果Observable发射了一个错误通知，重新订阅它，期待它正常终止
 * <p>
 * observable辅助操作（用于处理Observable的操作符）-------------------------------------------------------------------------------------------------------------------------------------------
 * Delay — 延迟一段时间发射结果数据
 * Do — 注册一个动作占用一些Observable的生命周期事件，相当于Mock某个操作
 * Materialize/Dematerialize — 将发射的数据和通知都当做数据发射，或者反过来
 * ObserveOn — 指定观察者观察Observable的调度程序（工作线程）
 * Serialize — 强制Observable按次序发射数据并且功能是有效的
 * Subscribe — 收到Observable发射的数据和通知后执行的操作
 * SubscribeOn — 指定Observable应该在哪个调度程序上执行
 * TimeInterval — 将一个Observable转换为发射两个数据之间所耗费时间的Observable
 * Timeout — 添加超时机制，如果过了指定的一段时间没有发射数据，就发射一个错误通知
 * Timestamp — 给Observable发射的每个数据项添加一个时间戳
 * Using — 创建一个只在Observable的生命周期内存在的一次性资源
 * <p>
 * observable条件和布尔操作（可用于单个或多个数据项，也可用于Observable）-------------------------------------------------------------------------------------------------------------------------------------------
 * All — 判断Observable发射的所有的数据项是否都满足某个条件
 * Amb — 给定多个Observable，只让第一个发射数据的Observable发射全部数据
 * Contains — 判断Observable是否会发射一个指定的数据项
 * DefaultIfEmpty — 发射来自原始Observable的数据，如果原始Observable没有发射数据，就发射一个默认数据
 * SequenceEqual — 判断两个Observable是否按相同的数据序列
 * SkipUntil — 丢弃原始Observable发射的数据，直到第二个Observable发射了一个数据，然后发射原始Observable的剩余数据
 * SkipWhile — 丢弃原始Observable发射的数据，直到一个特定的条件为假，然后发射原始Observable剩余的数据
 * TakeUntil — 发射来自原始Observable的数据，直到第二个Observable发射了一个数据或一个通知
 * TakeWhile — 发射原始Observable的数据，直到一个特定的条件为真，然后跳过剩余的数据
 * <p>
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

@LCheckActivity
public class InputEndValideActivity extends Activity implements View.OnClickListener, Observer {
    @LCheckView(R.id.et_name)
    EditText et_name;
    @LCheckView(R.id.et_phone)
    EditText et_phone;
    //    private EditText et_pwd , et_repwd , et_sex , et_email , et_work;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputend);
        LInputEndValideActivity.bindViewAndCheck(this);
//        et_name = findViewById(R.id.et_name);
//        et_phone = findViewById(R.id.et_phone);
//        et_pwd = findViewById(R.id.et_pwd);
//        et_repwd = findViewById(R.id.et_repwd);
//        et_sex = findViewById(R.id.et_sex);
//        et_email = findViewById(R.id.et_email);
//        et_work = findViewById(R.id.et_work);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
//
//        initObservable();
    }

//    @LBindClick(R.id.button)
//    public void onClick(){
//        Toast.makeText(this , "提交成功" , Toast.LENGTH_SHORT).show();
//    }

    private void initObservable() {
//        InitialValueObservable<CharSequence> observableName = RxTextView.textChanges(et_name);
//        InitialValueObservable<CharSequence> observablePhone = RxTextView.textChanges(et_phone);
//        InitialValueObservable<CharSequence> observablePwd = RxTextView.textChanges(et_pwd);
//        InitialValueObservable<CharSequence> observableRepwd = RxTextView.textChanges(et_repwd);
//        InitialValueObservable<CharSequence> observableSex = RxTextView.textChanges(et_sex);
//        InitialValueObservable<CharSequence> observableEmail = RxTextView.textChanges(et_email);
//        InitialValueObservable<CharSequence> observableWork = RxTextView.textChanges(et_work);
//        List<InitialValueObservable<CharSequence>> observableList = new ArrayList<>();
//        observableList.add(observableName);
//        observableList.add(observablePhone);
//        observableList.add(observablePwd);
//        observableList.add(observableRepwd);
//        observableList.add(observableSex);
//        observableList.add(observableEmail);
//        observableList.add(observableWork);
////        Observable.combineLatest(observableName, observablePhone, observablePwd, observableRepwd, observableSex, observableEmail
////                , observableWork , new Function7<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence , Boolean>() {
////                    @Override
////                    public Boolean apply(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, CharSequence charSequence6, CharSequence charSequence7) throws Exception {
////                        return !TextUtils.isEmpty(charSequence)
////                                && !TextUtils.isEmpty(charSequence2)
////                                && !TextUtils.isEmpty(charSequence3)
////                                && !TextUtils.isEmpty(charSequence4)
////                                && !TextUtils.isEmpty(charSequence5)
////                                && !TextUtils.isEmpty(charSequence6)
////                                && !TextUtils.isEmpty(charSequence7);
////                    }
////                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
////            @Override
////            public void onSubscribe(Disposable d) {
////
////            }
////
////            @Override
////            public void onNext(Boolean aBoolean) {
////                if(aBoolean){
////                    btn.setEnabled(true);
////                }else{
////                    btn.setEnabled(false);
////                }
////            }
////
////            @Override
////            public void onError(Throwable e) {
////
////            }
////
////            @Override
////            public void onComplete() {
////
////            }
////        });
//        Observable.combineLatest(observableList, new Function<Object[], Boolean>() {
//
//            @Override
//            public Boolean apply(Object[] objects) throws Exception {
//                for (Object o : objects){
//                    if(TextUtils.isEmpty((CharSequence) o)){
//                        return false;
//                    }
//                }
//                return true;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//                if(aBoolean){
//                    btn.setEnabled(true);
//                }else{
//                    btn.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


//        Observable<String> ab = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                if(!e.isDisposed()){
//                    e.onNext("result");
//                }
//
//                if(e.isDisposed()){
//                    e.onComplete();
//                }
//            }
//        });
//        ab.subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                //12-14 14:58:37.371 32596-32596/com.example.liming.validateproject E/LM: onNext--result
//                Log.e("LM" , "onNext--" + s);
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e("LM" , "onComplete--");
//            }
//        });
//        final List<String> data = new ArrayList<String>() ;
//        data.add("a");
//        data.add("ac");
//        data.add("acccc");
//        data.add("a3ddcc");
//        data.add("bnbbc");
//        data.add("9if");
//
//        RxTextView .textChanges(et_name)
//                .debounce(500 , TimeUnit.MILLISECONDS)
//                .map(new Function<CharSequence, String>() {
//                    @Override
//                    public String apply(CharSequence charSequence) throws Exception {
//                        return charSequence.toString();
//                    }
//                })
//                .observeOn(Schedulers.io())
////                .filter(new Predicate<String>() {
////                    @Override
////                    public boolean test(String s) throws Exception {
////                        return false;
////                    }
////                })
//                .map(new Function<String, List<String>>() {
//                    @Override
//                    public List<String> apply(String s) throws Exception {
//                        List<String> dataList = new ArrayList<String>() ;
//                        if ( !TextUtils.isEmpty(s)){
//                            //通过key s去过滤list集合
//                            for(String bean : data){
//                                if(bean.contains(s)){
//                                    dataList.add(bean);
//                                }
//                            }
//                        }
//                        return dataList;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<String>>() {
//                    @Override
//                    public void accept(List<String> strings) throws Exception {
//                        Log.e("LM" , "符合的数据个数  " + (strings == null ? 0 : strings.size()));
//                    }
//                });

        /**
         * Subject是一种bridge和proxy，它既是Observable又是Observer
         *
         * PublishSubject算是RxJava中最常用的Subject，一旦一个观察者订阅了该Subject，它会发送所有数据给订阅者。
         *
         * BehaviorSubject会发送离订阅最近的上一个值，没有上一个值的时候会发送默认值
         *
         * ReplaySubject，无论观察者何时订阅，ReplaySubject都会将所有内容发送给订阅者
         *
         * AsyncSubject无论发送多少个数据事件，观察者永远只能接受到最后一个数据(完成事件必须调用)。如果发送数据过程中出现错误，观察者仅仅接受到错误信息。
         */

    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void checkInput(boolean b) {
        if (b) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }
    }
}
