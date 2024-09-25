package com.example.munchkinaula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.munchkinaula.ui.theme.MunchkinAulaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MunchkinAulaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GerenciamentoJogadoresScreen()
                }
            }
        }
    }
}

@Composable
fun GerenciamentoJogadoresScreen() {
    val jogadores = remember { mutableStateListOf<Jogador>() }

    // Lista de jogadores
    if (jogadores.isEmpty()) {
        for (i in 1..6) {
            jogadores.add(Jogador(nome = "Jogador $i"))
        }
    }

    // Adiciona Scroll
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        jogadores.forEachIndexed { _, jogador ->
            JogadorView(jogador = jogador)

            // Adiciona um espaçamento e uma divisória após cada jogador
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun JogadorView(jogador: Jogador) {
    var nome by remember { mutableStateOf(jogador.nome) }
    var nivel by remember { mutableIntStateOf(jogador.nivel) }
    var bonusEquipamento by remember { mutableIntStateOf(jogador.bonusEquipamento) }
    var modificadores by remember { mutableIntStateOf(jogador.modificadores) }

    // O poder total é recalculado automaticamente sempre que nível, bônus ou modificadores mudam
    val poderTotal by remember {
        derivedStateOf { nivel + bonusEquipamento + modificadores }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        //Nome do jogador
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                jogador.nome = it
            },
            label = { Text("Nome do Jogador") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Nível
        AjusteValor("Nível", nivel, {
            nivel = it
            jogador.nivel = it
        }, 1, 10)

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Bônus de Equipamento
        AjusteValor("Bônus de Equipamento", bonusEquipamento, {
            bonusEquipamento = it
            jogador.bonusEquipamento = it
        })

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Modificadores
        AjusteValor("Modificadores", modificadores, {
            modificadores = it
            jogador.modificadores = it
        })

        Spacer(modifier = Modifier.height(8.dp))

        // Exibe o poder total
        Text(
            text = "Poder Total: $poderTotal",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun AjusteValor(label: String, valorAtual: Int, onValorChange: (Int) -> Unit, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) {
    var valor by remember { mutableIntStateOf(valorAtual) }

    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(text = "$label: $valor", modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            if (valor < max) {
                valor++
                onValorChange(valor)
            }
        }) {
            Text(text = "+")
        }

        Spacer(modifier = Modifier.width(4.dp))

        Button(onClick = {
            if (valor > min) {
                valor--
                onValorChange(valor)
            }
        }) {
            Text(text = "-")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MunchkinAulaTheme {
        GerenciamentoJogadoresScreen()
    }
}

data class Jogador(
    var nome: String,
    var nivel: Int = 1,
    var bonusEquipamento: Int = 0,
    var modificadores: Int = 0
)
