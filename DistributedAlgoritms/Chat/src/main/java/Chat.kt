package org.lab

import jdk.nashorn.internal.objects.Global.print
import org.jgroups.JChannel
import org.jgroups.Message
import org.jgroups.ReceiverAdapter
import org.jgroups.View
import org.jgroups.jmx.JmxConfigurator
import org.jgroups.util.Util
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.DriverManager.println

const val PROPS = "config.xml"

fun main(args: Array<String>) {

    val name = if (args.size == 1) {
        args[0]
    } else {
        throw IllegalArgumentException("must be 1 arg")
    }

    Chat().start(PROPS, name)
}

class Chat : ReceiverAdapter() {
    private var channel: JChannel? = null

    override fun viewAccepted(new_view: View?) {
        println("** view: $new_view")
    }

    override fun receive(msg: Message?) {
        val payload = msg!!.getObject<String>()
        println("[${msg.src}]: $payload")
    }

    fun start(props: String, name: String?) {
        channel = JChannel(props).name(name).receiver(this)
        channel!!.connect("ChatCluster")
        JmxConfigurator.registerChannel(channel!!, Util.getMBeanServer(), "chat-channel", channel!!.clusterName, true)
        eventLoop()
        channel!!.close()
    }

    private fun eventLoop() {
        val input = BufferedReader(InputStreamReader(System.`in`))
        while (true) {
            try {
                print("> ")
                System.out.flush()
                val line = input.readLine()
                if (line.startsWith(prefix = "quit") || line.startsWith(prefix = "exit")) {
                    break
                }
                val msg = Message(null, line)
                channel!!.send(msg)
            } catch (e: Exception) {
            }

        }
    }
}