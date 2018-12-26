package ru.nsu.fit.zavalishina

import org.jgroups.JChannel
import org.jgroups.ReceiverAdapter
import org.jgroups.View
import org.jgroups.blocks.MessageDispatcher
import org.jgroups.blocks.RequestOptions
import org.jgroups.blocks.RpcDispatcher
import org.jgroups.blocks.locking.LockService
import org.jgroups.util.Util

import java.io.*
import java.util.HashMap


fun main(args: Array<String>) {
    var props = "config.xml"
    var i = 0
    while (i < args.size) {
        if (args[i] == "-props") {
            props = args[++i]
            i++
            continue
        }
        println("ReplicatedStockServer [-props <XML config file>]")
        return
    }

    ReplicatedStockServer().start(props)
}

/**
 * Replicated stock server; every cluster node has the same state (stocks).
 */
class ReplicatedStockServer : ReceiverAdapter() {
    private val stocks = HashMap<String, Double>()
    private var channel: JChannel? = null
    private var lockService: LockService? = null
    private var disp: RpcDispatcher? = null // to invoke RPCs


    /** Assigns a value to a stock  */
    fun _setStock(name: String, value: Double) {
        synchronized(stocks) {
            stocks[name] = value
            System.out.printf("-- set %s to %s\n", name, value)
        }
    }

    /** Removes a stock from the hashmap  */
    fun _removeStock(name: String) {
        synchronized(stocks) {
            stocks.remove(name)
            System.out.printf("-- removed %s\n", name)
        }
    }

    fun start(props: String) {
        channel = JChannel(props)
        disp = RpcDispatcher(channel, this).setMembershipListener(this)
        lockService = LockService(channel)
        disp?.setStateListener<MessageDispatcher>(this)
        channel?.connect("stocks")
        disp?.start<MessageDispatcher>()
        channel?.getState(null, 30000) // fetches the state from the coordinator
        while (true) {
            val c = Util.keyPress("[1] Show stocks [2] Get quote [3] Set quote [4] Remove quote [5] CAS [x] Exit")
            try {
                when (c) {
                    '1'.toInt() -> showStocks()
                    '2'.toInt() -> getStock()
                    '3'.toInt() -> setStock()
                    '4'.toInt() -> removeStock()
                    '5'.toInt() -> CAS()
                    'x'.toInt() -> {
                        channel!!.close()
                        return
                    }
                }
            } catch (ex: Exception) {
            }

        }
    }

    /**
     * Set a stock if it's value equal to ref
     */
    fun _CAS(name: String, reference: Double, newValue: Double): Boolean {
        synchronized(stocks) {
            val value = stocks[name]
            if (value == reference) {
                stocks[name] = newValue
                System.out.printf("-- cas -- set %s to %s\n", name, newValue)
                return true
            } else {
                System.out.printf("-- cas -- failed in setting %s to %s\n", name, newValue)
                return false
            }
        }
    }

    /** Compare value and swap it if value by key == reference value  */
    private fun CAS(): Boolean {
        val key = readString("key")
        val referenceValue = readString("referenceValue")
        val refVal = java.lang.Double.parseDouble(referenceValue)
        val newValue = readString("newValue")
        val lock = lockService!!.getLock("lock $key")
        lock.lock()
        try {
            val rsps = disp!!.callRemoteMethods<Boolean>(
                null, "_CAS",
                arrayOf(key, refVal, java.lang.Double.parseDouble(newValue)),
                arrayOf(String::class.java, Double::class.java, Double::class.java), RequestOptions.SYNC()
            )


            var countSuccessed = 0

            for (v in rsps) {
                if (v.wasReceived() && v.value == true) {
                    countSuccessed++
                }
            }

            val successed = countSuccessed > rsps.numReceived() / 2

            println("cas: " + key + "set to " + newValue + if (successed) "Successfully" else "Failed")
            return successed
        } finally {
            lock.unlock()
        }
    }

    override fun viewAccepted(view: View?) {
        println("-- VIEW: " + view!!)
    }

    override fun getState(output: OutputStream?) {
        val out = DataOutputStream(output)
        synchronized(stocks) {
            println("-- returning " + stocks.size + " stocks")
            Util.objectToStream(stocks, out)
        }
    }

    override fun setState(input: InputStream?) {
        val `in` = DataInputStream(input!!)
        val new_state = Util.objectFromStream<Map<String, Double>>(`in`)
        println("-- received state: " + new_state.size + " stocks")
        synchronized(stocks) {
            stocks.clear()
            stocks.putAll(new_state)
        }
    }

    private fun getStock() {
        val ticker = readString("Symbol")
        synchronized(stocks) {
            val `val` = stocks[ticker]
            println("$ticker is $`val`")
        }
    }

    private fun setStock() {
        val ticker: String = readString("Symbol")
        val value: String = readString("Value")
        val rsps = disp!!.callRemoteMethods<Void>(
            null, "_setStock", arrayOf(ticker, java.lang.Double.parseDouble(value)),
            arrayOf<Class<*>>(String::class.java, Double::class.java), RequestOptions.SYNC()
        )
        println("rsps:\n$rsps")
    }

    private fun removeStock() {
        val ticker = readString("Symbol")
        val rsps = disp!!.callRemoteMethods<Void>(
            null, "_removeStock", arrayOf<Any>(ticker),
            arrayOf<Class<*>>(String::class.java), RequestOptions.SYNC()
        )
        println("rsps:\n$rsps")
    }

    private fun showStocks() {
        println("Stocks:")
        synchronized(stocks) {
            for ((key, value) in stocks) {
                println("$key: $value")
            }
        }
    }

    companion object {

        private fun readString(s: String): String {
            var c: Int
            var looping = true
            val sb = StringBuilder()
            print("$s: ")
            System.out.flush()
            System.`in`.skip(System.`in`.available().toLong())

            while (looping) {
                c = System.`in`.read()
                when (c) {
                    -1, '\n'.toInt(), 13 -> looping = false
                    else -> sb.append(c.toChar())
                }
            }

            return sb.toString()
        }
    }


}
