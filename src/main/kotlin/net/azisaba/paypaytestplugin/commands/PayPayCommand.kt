package net.azisaba.paypaytestplugin.commands

import jp.ne.paypay.ApiException
import jp.ne.paypay.model.MoneyAmount
import jp.ne.paypay.model.QRCode
import net.azisaba.paypaytestplugin.PayPayTestPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.*

class PayPayCommand(private val plugin: PayPayTestPlugin) : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }
        if (args[0] == "create") {
            if (args.size <= 2) {
                sender.sendMessage("${ChatColor.RED}/paypay create <amount> <description>")
                return true
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                val qrCode = QRCode()
                val paymentId = UUID.randomUUID().toString()
                qrCode.amount = MoneyAmount().amount(args[1].toInt()).currency(MoneyAmount.CurrencyEnum.JPY)
                qrCode.merchantPaymentId = paymentId
                qrCode.codeType = "ORDER_QR"
                qrCode.orderDescription = args[2]
                qrCode.isAuthorization(false)
                val details = try {
                    plugin.paymentApi.createQRCode(qrCode)
                } catch (e: ApiException) {
                    plugin.slF4JLogger.error("Error returned from PayPay API, response body: ${e.responseBody}")
                    return@Runnable
                }
                if (details.resultInfo.code == "SUCCESS") {
                    sender.sendMessage("${ChatColor.AQUA}${ChatColor.UNDERLINE}${details.data.url}${ChatColor.GREEN}を開いて、支払いを行ってください。")
                } else {
                    sender.sendMessage("${ChatColor.RED}エラーが発生しました。コード: ${details.resultInfo.code}")
                }
                plugin.startPollingPayment(sender.uniqueId, paymentId)
            })
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (args.size == 1) return listOf("create").filter { it.startsWith(args[0]) }
        return emptyList()
    }
}
