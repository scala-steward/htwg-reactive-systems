package myPackage

import tui.*
import tui.widgets.*
import tui.widgets.canvas.*
import tui.widgets.tabs.TabsWidget

object ui:
  def draw(f: Frame, app: App): Unit =
    val chunks = Layout(constraints =
      Array(Constraint.Length(3), Constraint.Min(0))
    ).split(f.size)
    val titles = app.tabs.titles.map(t =>
      Spans.from(Span.styled(t, Style.DEFAULT.fg(Color.Green)))
    )

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
      case 0 => draw_first_tab(f, app, chunks(1))
      case 1 => draw_second_tab(f, app, chunks(1))
      case _ =>

  def draw_first_tab(f: Frame, app: App, area: Rect): Unit =
    val chunks = Layout(
      direction = Direction.Horizontal,
      constraints = Array(Constraint.Ratio(1, 1))
    ).split(area)
    val items = app.countries match
      case Left(countries) =>
        countries.map { c =>
          val cells = Array(
            TableWidget.Cell(Text.nostyle(c.name)),
            TableWidget.Cell(Text.nostyle(c.code)),
            TableWidget.Cell(Text.nostyle(c.services.mkString(", ")))
          )
          TableWidget.Row(cells)
        }
      case Right(error) =>
        val cells = Array(
          TableWidget.Cell(Text.nostyle(error)),
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

  def draw_second_tab(f: Frame, app: App, area: Rect): Unit =
    val chunks = Layout(
      direction = Direction.Horizontal,
      constraints = Array(Constraint.Ratio(1, 1))
    ).split(area)

    val table = TableWidget(
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
