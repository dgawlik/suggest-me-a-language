package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language
import org.dgawlik.parsing.Parser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class Id3TreeTest{

    val f1 = Feature("LANG1", "first feature", BinaryField(), 1)
    val f2 = Feature("LANG2", "second feature", BinaryField(), 1)

    @Test
    @DisplayName("should create valid composite criteria")
    fun composite_criteria(){
        val array = arrayOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1), FeatureRealization(f2, 0)), ""),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0), FeatureRealization(f2, 1)), "")
        )


        val root = TreeNode(null, null, null, FeatureRealization(f1, 1))
        root.left =  TreeNode(root, null, null, null)
        root.right = TreeNode(root, null, null, null)

        val criteria = Id3Tree.compositeCriteria(root.right!!)

        val result = array.filter {criteria(it)}

        assertEquals(1, result.size)
        assertEquals("L1", result[0].name)
    }

    @Test
    @DisplayName("should create tree")
    fun create_tree(){
        val db = BinaryField::class.java.getResource("/Database.md")!!.readText()
        val parser = Parser(db)
        parser.parse()

        val cart = Id3Tree(parser.languages, parser.features)
        assertNotNull(cart.root)
    }
}