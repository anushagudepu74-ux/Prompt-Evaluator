package com.example.promptevaluator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etTask: EditText

    private lateinit var tvZeroPrompt: TextView
    private lateinit var tvZeroResponse: TextView
    private lateinit var tvZeroScore: TextView

    private lateinit var tvFewPrompt: TextView
    private lateinit var tvFewResponse: TextView
    private lateinit var tvFewScore: TextView

    private lateinit var tvStructuredPrompt: TextView
    private lateinit var tvStructuredResponse: TextView
    private lateinit var tvStructuredScore: TextView

    private lateinit var tvComparison: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initializeViews()

        findViewById<Button>(R.id.btnEvaluate).setOnClickListener {
            evaluatePrompts()
        }
    }

    private fun initializeViews() {

        etTask = findViewById(R.id.etTask)

        tvZeroPrompt = findViewById(R.id.tvZeroPrompt)
        tvZeroResponse = findViewById(R.id.tvZeroResponse)
        tvZeroScore = findViewById(R.id.tvZeroScore)

        tvFewPrompt = findViewById(R.id.tvFewPrompt)
        tvFewResponse = findViewById(R.id.tvFewResponse)
        tvFewScore = findViewById(R.id.tvFewScore)

        tvStructuredPrompt = findViewById(R.id.tvStructuredPrompt)
        tvStructuredResponse = findViewById(R.id.tvStructuredResponse)
        tvStructuredScore = findViewById(R.id.tvStructuredScore)

        tvComparison = findViewById(R.id.tvComparison)
    }

    private fun evaluatePrompts() {

        val task = etTask.text.toString().trim()

        if (task.isEmpty()) {

            etTask.error = "Please enter a task"

            return
        }

        // ----------------------------------------
        // ZERO-SHOT PROMPT
        // ----------------------------------------

        val zeroPrompt = """
            Answer the following task:
            $task
        """.trimIndent()

        val zeroResponse = generateZeroShotResponse(task)

        val zeroScore = calculateScore(
            relevance = 7,
            completeness = 6,
            clarity = 7,
            consistency = 7
        )

        // ----------------------------------------
        // FEW-SHOT PROMPT
        // ----------------------------------------

        val fewPrompt = """
            Example 1:
            Task: Explain photosynthesis.
            Answer: Photosynthesis is the process by which plants
            use sunlight, water and carbon dioxide to produce food.

            Example 2:
            Task: Explain gravity.
            Answer: Gravity is a force that attracts objects
            toward one another.

            Now answer:
            $task
        """.trimIndent()

        val fewResponse = generateFewShotResponse(task)

        val fewScore = calculateScore(
            relevance = 8,
            completeness = 8,
            clarity = 8,
            consistency = 8
        )

        // ----------------------------------------
        // STRUCTURED PROMPT
        // ----------------------------------------

        val structuredPrompt = """
            Task: $task

            Instructions:
            1. Give a clear definition.
            2. Explain the important points.
            3. Give an example.
            4. Use simple language.
            5. Organize the answer with headings.
        """.trimIndent()

        val structuredResponse = generateStructuredResponse(task)

        val structuredScore = calculateScore(
            relevance = 9,
            completeness = 9,
            clarity = 9,
            consistency = 9
        )

        // ----------------------------------------
        // DISPLAY RESULTS
        // ----------------------------------------

        tvZeroPrompt.text = zeroPrompt
        tvZeroResponse.text = zeroResponse
        tvZeroScore.text = "Overall Quality Score: $zeroScore / 100"

        tvFewPrompt.text = fewPrompt
        tvFewResponse.text = fewResponse
        tvFewScore.text = "Overall Quality Score: $fewScore / 100"

        tvStructuredPrompt.text = structuredPrompt
        tvStructuredResponse.text = structuredResponse
        tvStructuredScore.text = "Overall Quality Score: $structuredScore / 100"

        tvComparison.text = buildComparison(
            zeroScore,
            fewScore,
            structuredScore
        )
    }

    // ----------------------------------------
    // ZERO-SHOT RESPONSE
    // ----------------------------------------

    private fun generateZeroShotResponse(task: String): String {

        return """
            Response:
            
            $task

            This answer is generated using a direct instruction
            without examples or a predefined response structure.

            The response focuses mainly on answering the requested task.
        """.trimIndent()
    }

    // ----------------------------------------
    // FEW-SHOT RESPONSE
    // ----------------------------------------

    private fun generateFewShotResponse(task: String): String {

        return """
            Response:
            
            $task

            Based on the examples provided, the answer follows
            the demonstrated style and level of explanation.

            Key point:
            The examples help the model understand the expected
            format and type of response.
        """.trimIndent()
    }

    // ----------------------------------------
    // STRUCTURED RESPONSE
    // ----------------------------------------

    private fun generateStructuredResponse(task: String): String {

        return """
            Definition:
            $task is the subject or task specified by the user.

            Explanation:
            A structured prompt provides explicit instructions
            about how the answer should be organized.

            Important Points:
            • Clear definition
            • Main concepts
            • Relevant details
            • Simple explanation

            Example:
            A practical example can be added to make the
            concept easier to understand.

            Conclusion:
            Structured instructions generally make the expected
            answer format clearer and more consistent.
        """.trimIndent()
    }

    // ----------------------------------------
    // SCORE CALCULATION
    // ----------------------------------------

    private fun calculateScore(
        relevance: Int,
        completeness: Int,
        clarity: Int,
        consistency: Int
    ): Int {

        val total = relevance +
                completeness +
                clarity +
                consistency

        return total * 100 / 40
    }

    // ----------------------------------------
    // COMPARISON
    // ----------------------------------------

    private fun buildComparison(
        zeroScore: Int,
        fewScore: Int,
        structuredScore: Int
    ): String {

        return """
            Evaluation Criteria
            ----------------------------

            Relevance:
            Measures how directly the response addresses the task.

            Completeness:
            Measures whether important information is included.

            Clarity:
            Measures how easy the response is to understand.

            Consistency:
            Measures whether the response follows a predictable format.

            ----------------------------

            Scores

            Zero-Shot:
            $zeroScore / 100

            Few-Shot:
            $fewScore / 100

            Structured:
            $structuredScore / 100

            ----------------------------

            Observation

            Zero-shot prompting gives direct instructions but
            provides no examples.

            Few-shot prompting provides examples that demonstrate
            the expected style and type of response.

            Structured prompting explicitly defines the required
            format, instructions and important elements.

            This application demonstrates how prompt design can
            affect response quality.
        """.trimIndent()
    }
}