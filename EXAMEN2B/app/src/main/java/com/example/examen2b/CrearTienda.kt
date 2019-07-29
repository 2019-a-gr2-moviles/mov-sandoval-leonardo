package com.example.examen2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_crear_tienda.*

class CrearTienda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tienda)
        btn_ins_pac.setOnClickListener {
            try {
                val tienda = Tienda(
                    null,
                    -1,
                    txt_nombres_tienda.text.toString(),
                    txt_ape_pac.text.toString(),
                    dp_fec_nac_pac.text.toString(),
                    txt_hij_pac.text.toString().toInt(),
                    sw_seg_pac.text.toString().toBoolean()
                )
                ingresarTienda(tienda)
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error al Registrar: ${ClassAux.nombreUsuario}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun ingresarTienda(tienda: Tienda) {
        try {
            val url = Conexion.url("tienda")
            val json = """
            {
            "nombres": "${tienda.nombres}",
            "apellidos": "${tienda.apellidos}",
            "fechaNacimiento": "${tienda.fechaNacimiento}",
            "hijos": ${tienda.hijos},
            "tieneSeguro" : ${tienda.tieneSeguro}
                                         }
                    """.trimIndent()

            Log.i("http", json)
            url.httpPost().body(json)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.i("http", "Error: ${ex.message}")
                        }
                        is Result.Success -> {
                            runOnUiThread {
                                Toast.makeText(
                                    this,
                                    "Registro Exitoso: ${ClassAux.nombreUsuario}",
                                    Toast.LENGTH_LONG
                                ).show()
                                irAListaTiendas()
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al Registrar: ${ClassAux.nombreUsuario}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun irAListaTiendas() {
        val intent = Intent(
            this,
            ListaTiendas::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
