package com.example.oxideespgreen

import android.app.Activity
import android.os.Bundle
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.*
import android.content.Context

class MainActivity : Activity() {
    private lateinit var scene: DemoScene
    private var espEnabled = true
    private var distanceEnabled = true
    private var colorIndex = 0
    private val colors = intArrayOf(Color.rgb(55, 235, 145), Color.CYAN, Color.MAGENTA, Color.YELLOW)
    private val colorNames = arrayOf("NEON GREEN", "CYAN", "MAGENTA", "YELLOW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 26, 20, 20)
            setBackgroundColor(Color.rgb(8, 12, 11))
        }
        val header = TextView(this).apply {
            text = "OXIDE  /  ESP"
            textSize = 25f
            setTextColor(Color.rgb(70, 245, 155))
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
        }
        page.addView(header)
        val sub = TextView(this).apply {
            text = "ANDROID TOOLKIT  •  DEMO BUILD 1.17.11995"
            textSize = 10f
            letterSpacing = .12f
            setTextColor(Color.rgb(145, 165, 153))
            setPadding(0, 4, 0, 20)
        }
        page.addView(sub)

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 14, 16, 14)
            setBackgroundColor(Color.rgb(19, 28, 23))
        }
        val status = TextView(this).apply {
            text = "●  DEMO SCENE ACTIVE"
            textSize = 12f
            setTextColor(Color.rgb(70, 245, 155))
            typeface = Typeface.DEFAULT_BOLD
        }
        card.addView(status)
        val hint = TextView(this).apply {
            text = "Визуализация на искусственных объектах. Приложение не подключается к игре."
            textSize = 12f
            setTextColor(Color.LTGRAY)
            setPadding(0, 8, 0, 0)
        }
        card.addView(hint)
        page.addView(card)

        val controls = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 18, 0, 10)
        }
        val espSwitch = Switch(this).apply {
            text = "ESP"
            isChecked = true
            setTextColor(Color.WHITE)
            buttonTintList = android.content.res.ColorStateList.valueOf(Color.rgb(70,245,155))
            setOnCheckedChangeListener { _, checked -> espEnabled = checked; scene.invalidate() }
        }
        val distanceSwitch = Switch(this).apply {
            text = "DISTANCE"
            isChecked = true
            setTextColor(Color.WHITE)
            buttonTintList = android.content.res.ColorStateList.valueOf(Color.rgb(70,245,155))
            setOnCheckedChangeListener { _, checked -> distanceEnabled = checked; scene.invalidate() }
        }
        controls.addView(espSwitch, LinearLayout.LayoutParams(0, -2, 1f))
        controls.addView(distanceSwitch, LinearLayout.LayoutParams(0, -2, 1f))
        page.addView(controls)

        val colorButton = Button(this).apply {
            text = "ESP COLOR  •  NEON GREEN"
            setTextColor(Color.rgb(70,245,155))
            setBackgroundColor(Color.rgb(25, 43, 33))
            setOnClickListener {
                colorIndex = (colorIndex + 1) % colors.size
                text = "ESP COLOR  •  ${colorNames[colorIndex]}"
                scene.setEspColor(colors[colorIndex])
            }
        }
        page.addView(colorButton)

        val previewTitle = TextView(this).apply {
            text = "LIVE PREVIEW"
            textSize = 11f
            letterSpacing = .15f
            setTextColor(Color.rgb(145, 165, 153))
            setPadding(0, 18, 0, 8)
        }
        page.addView(previewTitle)
        scene = DemoScene(this)
        scene.setEspColor(colors[0])
        page.addView(scene, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(page)
    }

    private class DemoScene(context: Context) : View(context) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private var espColor = Color.rgb(55, 235, 145)
        fun setEspColor(c: Int) { espColor = c; invalidate() }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.rgb(13, 21, 17))
            p.style = Paint.Style.FILL
            p.color = Color.WHITE
            p.textSize = 17f
            p.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("TRAINING TARGETS", 18f, 30f, p)
            val host = context as? MainActivity ?: return
            if (!host.espEnabled) {
                p.color = Color.LTGRAY
                p.textSize = 14f
                canvas.drawText("ESP DISABLED", 18f, 65f, p)
                return
            }
            val items = listOf(Triple(26f, 75f, "TARGET A"), Triple(190f, 145f, "TARGET B"), Triple(55f, 285f, "TARGET C"))
            for ((x, y, name) in items) {
                p.style = Paint.Style.STROKE
                p.strokeWidth = 3f
                p.color = espColor
                canvas.drawRect(x, y, x + 105f, y + 125f, p)
                p.style = Paint.Style.FILL
                p.textSize = 13f
                p.color = Color.WHITE
                val label = if (host.distanceEnabled) "$name  •  ${(y/8).toInt()+8} m" else name
                canvas.drawText(label, x, y - 9f, p)
            }
        }
    }
}
