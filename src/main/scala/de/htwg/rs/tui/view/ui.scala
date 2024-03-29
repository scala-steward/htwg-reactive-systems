package de.htwg.rs.tui.view

import de.htwg.rs.tui.controller.App

import scala.util.{Failure, Success}

import tui.*
import tui.widgets.*
import tui.widgets.tabs.TabsWidget

object ui:
  def draw(f: Frame, app: App): Unit =
    val chunks = Layout(constraints =
      Array(Constraint.Length(3), Constraint.Min(0))
    ).split(f.size)
    val titles = app.tabs.titles
      .map(t => Spans.from(Span.styled(t, Style.DEFAULT.fg(Color.Green))))
      .toArray

    val tabs = TabsWidget(
      titles = titles,
      block = Some(
        BlockWidget(
          borders = Borders.ALL,
          title = Some(Spans.nostyle(app.title))
        )
      ),
      highlightStyle = Style.DEFAULT.fg(Color.Yellow),
      selected = app.tabs.index
    )
    f.renderWidget(tabs, chunks(0))
    app.tabs.index match
      case 0 => drawFirstTab(f, app, chunks(1))
      case 1 => drawSecondTab(f, app, chunks(1))
      case 2 => drawThirdTab(f, app, chunks(1))
      case _ =>

  def drawFirstTab(f: Frame, app: App, area: Rect): Unit =
    val chunks = Layout(
      direction = Direction.Horizontal,
      constraints = Array(Constraint.Ratio(1, 1))
    ).split(area)
    val items = app.countries match
      case Success(countries) =>
        countries.map { c =>
          val cells = Array(
            TableWidget.Cell(Text.nostyle(c.name)),
            TableWidget.Cell(Text.nostyle(c.code)),
            TableWidget.Cell(Text.nostyle(c.servicesAsList.mkString(", ")))
          )
          TableWidget.Row(cells)
        }.toArray
      case Failure(error) =>
        val cells = Array(
          TableWidget.Cell(Text.nostyle(error.getMessage)),
          TableWidget.Cell(Text.nostyle("")),
          TableWidget.Cell(Text.nostyle(""))
        )
        Array(TableWidget.Row(cells))

    val table = TableWidget(
      rows = items,
      header = Some(
        TableWidget.Row(
          cells = Array(
            TableWidget.Cell(Text.nostyle("Name")),
            TableWidget.Cell(Text.nostyle("Code")),
            TableWidget.Cell(Text.nostyle("Services"))
          ),
          style = Style.DEFAULT.fg(Color.Yellow),
          bottomMargin = 1
        )
      ),
      block = Some(
        BlockWidget(
          title = Some(Spans.nostyle("Countries")),
          borders = Borders.ALL
        )
      ),
      widths = Array(
        Constraint.Ratio(1, 3),
        Constraint.Ratio(1, 3),
        Constraint.Ratio(1, 3)
      )
    )
    f.renderWidget(table, chunks(0))

def drawSecondTab(f: Frame, app: App, area: Rect): Unit =
  val layout = Layout(
    direction = Direction.Horizontal,
    constraints = Array(Constraint.Ratio(1, 2), Constraint.Ratio(1, 2))
  )

  val chunks = layout.split(area)
  // Create separate tables for the first two and the last two columns
  val streamingProviderTable = TableWidget(
    rows =
      app.streamingProviderSpread.map { case (provider, spreadPercentage) =>
        val cells = Array(
          TableWidget.Cell(Text.nostyle(provider)),
          TableWidget.Cell(Text.nostyle(spreadPercentage.toString))
        )
        TableWidget.Row(cells)
      }.toArray,
    header = Some(
      TableWidget.Row(
        cells = Array(
          TableWidget.Cell(Text.nostyle("Streaming Provider")),
          TableWidget.Cell(Text.nostyle("Percentage"))
        ),
        style = Style.DEFAULT.fg(Color.Yellow),
        bottomMargin = 1
      )
    ),
    block = Some(
      BlockWidget(
        title = Some(Spans.nostyle("Stats across Countries")),
        borders = Borders.ALL
      )
    ),
    widths = Array(
      Constraint.Ratio(1, 2),
      Constraint.Ratio(1, 2)
    )
  )

  val paymentMethodTable = TableWidget(
    rows = app.streamingProviderPaymentModelSpread.map {
      case (provider, paymentMethod) =>
        val cells = Array(
          TableWidget.Cell(Text.nostyle(provider)),
          TableWidget.Cell(Text.nostyle(paymentMethod.toString))
        )
        TableWidget.Row(cells)
    }.toArray,
    header = Some(
      TableWidget.Row(
        cells = Array(
          TableWidget.Cell(Text.nostyle("Payment Method")),
          TableWidget.Cell(Text.nostyle("Percentage"))
        ),
        style = Style.DEFAULT.fg(Color.Yellow),
        bottomMargin = 1
      )
    ),
    block = Some(
      BlockWidget(
        title = Some(Spans.nostyle("Payment Methods")),
        borders = Borders.ALL
      )
    ),
    widths = Array(
      Constraint.Ratio(1, 2),
      Constraint.Ratio(1, 2)
    )
  )

  // Render the two tables side by side
  f.renderWidget(streamingProviderTable, chunks(0))
  f.renderWidget(paymentMethodTable, chunks(1))

def drawThirdTab(f: Frame, app: App, area: Rect): Unit =
  val layout = Layout(
    direction = Direction.Horizontal,
    constraints = Array(Constraint.Ratio(1, 2), Constraint.Ratio(1, 2))
  )

  val chunks = layout.split(area)
  // Create separate tables for the first two and the last two columns
  val countNewChangesProviderTable = TableWidget(
    rows = app.countNewChangesProvider.map { case (provider, amountChanges) =>
      val cells = Array(
        TableWidget.Cell(Text.nostyle(provider)),
        TableWidget.Cell(Text.nostyle(amountChanges.toString))
      )
      TableWidget.Row(cells)
    }.toArray,
    header = Some(
      TableWidget.Row(
        cells = Array(
          TableWidget.Cell(Text.nostyle("Streaming Service")),
          TableWidget.Cell(Text.nostyle("new movies in last 15 days"))
        ),
        style = Style.DEFAULT.fg(Color.Yellow),
        bottomMargin = 1
      )
    ),
    block = Some(
      BlockWidget(
        title = Some(Spans.nostyle("Stats about changes in last 15 days")),
        borders = Borders.ALL
      )
    ),
    widths = Array(
      Constraint.Ratio(1, 2),
      Constraint.Ratio(1, 2)
    )
  )

  val countRemovedChangesProviderTable = TableWidget(
    rows =
      app.countRemovedChangesProvider.map { case (provider, amountChanges) =>
        val cells = Array(
          TableWidget.Cell(Text.nostyle(provider)),
          TableWidget.Cell(Text.nostyle(amountChanges.toString))
        )
        TableWidget.Row(cells)
      }.toArray,
    header = Some(
      TableWidget.Row(
        cells = Array(
          TableWidget.Cell(Text.nostyle("Streaming Service")),
          TableWidget.Cell(Text.nostyle("removed movies in last 15 days"))
        ),
        style = Style.DEFAULT.fg(Color.Yellow),
        bottomMargin = 1
      )
    ),
    block = Some(
      BlockWidget(
        title = Some(Spans.nostyle("Stats about changes in last 15 days")),
        borders = Borders.ALL
      )
    ),
    widths = Array(
      Constraint.Ratio(1, 2),
      Constraint.Ratio(1, 2)
    )
  )

  // Render the two tables side by side
  f.renderWidget(countNewChangesProviderTable, chunks(0))
  f.renderWidget(countRemovedChangesProviderTable, chunks(1))
