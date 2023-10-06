package net.azisaba.paypaytestplugin

import jp.ne.paypay.ApiClient
import jp.ne.paypay.Configuration
import jp.ne.paypay.api.PaymentApi
import jp.ne.paypay.model.PaymentState
import net.azisaba.paypaytestplugin.commands.PayPayCommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.properties.Delegates

class PayPayTestPlugin : JavaPlugin() {
    private var production by Delegates.notNull<Boolean>()
    private lateinit var apiKey: String
    private lateinit var apiSecretKey: String
    private lateinit var merchantId: String
    private lateinit var apiClient: ApiClient
    lateinit var paymentApi: PaymentApi

    override fun onEnable() {
        saveDefaultConfig()
        production = config.getBoolean("production", false)
        apiKey = config.getString("api-key", "")!!
        apiSecretKey = config.getString("api-secret-key", "")!!
        merchantId = config.getString("merchant-id", "")!!
        apiClient = Configuration().defaultApiClient
        apiClient.isProductionMode = production
        apiClient.setApiKey(apiKey)
        apiClient.setApiSecretKey(apiSecretKey)
        apiClient.setAssumeMerchant(merchantId)
        paymentApi = PaymentApi(apiClient)
        getCommand("paypay")!!.setExecutor(PayPayCommand(this))
    }

    fun startPollingPayment(player: UUID, id: String) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    val response = paymentApi.getCodesPaymentDetails(id)
                    if (response.resultInfo.code != "SUCCESS") {
                        return cancel()
                    }
                    if (response.data.status != PaymentState.StatusEnum.CREATED) {
                        cancel()
                    }
                    if (response.data.status == PaymentState.StatusEnum.COMPLETED) {
                        Bukkit.getPlayer(player)
                            ?.sendMessage("${ChatColor.GREEN}${response.data.orderDescription}の購入が完了しました！")
                    }
                } catch (e: Exception) {
                    cancel()
                }
            }
        }.runTaskTimerAsynchronously(this, 20 * 5, 20 * 5)
    }
}
